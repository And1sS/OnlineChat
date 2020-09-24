package com.and1ss.onlinechat.services.chat;

import com.and1ss.onlinechat.exceptions.BadRequestException;
import com.and1ss.onlinechat.exceptions.UnauthorizedException;
import com.and1ss.onlinechat.exceptions.UnimplementedException;
import com.and1ss.onlinechat.services.chat.model.group_chat.GroupChat;
import com.and1ss.onlinechat.services.chat.model.group_chat.GroupChatUser;
import com.and1ss.onlinechat.services.chat.model.group_chat.GroupChatUserId;
import com.and1ss.onlinechat.services.chat.model.private_chat.PrivateChat;
import com.and1ss.onlinechat.services.chat.repos.GroupChatRepository;
import com.and1ss.onlinechat.services.chat.repos.GroupChatUserRepository;
import com.and1ss.onlinechat.services.chat.repos.PrivateChatRepository;
import com.and1ss.onlinechat.services.user.UserService;
import com.and1ss.onlinechat.services.user.model.AccountInfo;
import com.and1ss.onlinechat.services.chat.model.group_chat.GroupChatUser.MemberType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private PrivateChatRepository privateChatRepository;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private GroupChatUserRepository groupChatUserJoinRepository;

    @Autowired
    private UserService userService;

    @Override
    public PrivateChat createPrivateChat(PrivateChat chat, AccountInfo author) {
        if (!chat.getUser1().equals(author) && !chat.getUser2().equals(author)) {
            throw new UnauthorizedException("This user can't create this chat");
        }

        PrivateChat privateChat;
        try {
            privateChat = privateChatRepository.save(chat);
        } catch (Exception e) {
            throw new BadRequestException("This chat is already present");
        }

        return privateChat;
    }

    @Override
    public PrivateChat getPrivateChatById(UUID id, AccountInfo author) {
        PrivateChat chat;
        try {
            chat = privateChatRepository.getOne(id);
        } catch (Exception e) {
            throw new UnauthorizedException("This user is not allowed to view this chat");
        }

        if (!userMemberOfPrivateChat(chat, author)) {
            throw new UnauthorizedException("This user is not allowed to view this chat");
        }

        return chat;
    }

    @Override
    public List<PrivateChat> getAllPrivateChatsForUser(AccountInfo user) {
        return privateChatRepository.findPrivateChatsByUserId(user.getId());
    }

    @Override
    public List<PrivateChat> getPrivateChatsPageForUser(AccountInfo user) {
        throw new UnimplementedException();
    }

    @Override
    public boolean userMemberOfPrivateChat(PrivateChat chat, AccountInfo author) {
        return chat.getUser1().equals(author) || chat.getUser2().equals(author);
    }

    @Override
    public GroupChat createGroupChat(
            GroupChat chat,
            List<AccountInfo> participants,
            AccountInfo author
    ) {
        if (chat.getTitle().isEmpty()) {
            throw new BadRequestException("Group chat must have not empty title");
        }

        if (!participants.contains(author)) {
            throw new BadRequestException("Creator should also be a participant");
        }

        GroupChat createdChat;
        try {
            createdChat = groupChatRepository.save(chat);
        } catch (Exception e) {
            throw new BadRequestException("This chat is already present");
        }

        uncheckedAddUsers(chat, author, participants);

        return createdChat;
    }

    @Override
    public boolean userMemberOfGroupChat(GroupChat chat, AccountInfo user) {
        GroupChatUser join = groupChatUserJoinRepository
                .findByGroupChatIdAndUserId(chat.getId(), user.getId());
        return join != null;
    }

    @Override
    public List<AccountInfo> getGroupChatMembers(GroupChat chat, AccountInfo author) {
        if (!userMemberOfGroupChat(chat, author)) {
            throw new UnauthorizedException("This user is not allowed to view this chat");
        }
        List<GroupChatUser> joins = groupChatUserJoinRepository.findAllByChatId(chat.getId());
        List<UUID> usersIds = joins.stream()
                .map(join -> join.getId().getUserId())
                .collect(Collectors.toList());

        return userService.findUsersByListOfIds(usersIds);
    }

    @Override
    public GroupChat getGroupChatById(UUID id, AccountInfo author)
    {
        GroupChat chat;
        try {
            chat = groupChatRepository.getOne(id);
        } catch (Exception e) {
            throw new UnauthorizedException("This user is not allowed to view this chat");
        }

        if (!userMemberOfGroupChat(chat, author)) {
            throw new UnauthorizedException("This user is not allowed to view this chat");
        }

        return chat;
    }

    @Override
    public void addUser(GroupChat chat, AccountInfo author, AccountInfo toBeAdded) {
        if (groupChatRepository.findGroupChatById(chat.getId()) == null) {
            throw new BadRequestException("This chat does not exist");
        }

        if (!userMemberOfGroupChat(chat, author)) {
            throw new UnauthorizedException("This user cannot add users to this chat");
        }

        if (!userMemberOfGroupChat(chat, toBeAdded)) {
            GroupChatUserId compositeId = new GroupChatUserId(
                    chat.getId(),
                    toBeAdded.getId()
            );

            GroupChatUser join = GroupChatUser.builder()
                    .memberType(MemberType.readwrite)
                    .id(compositeId)
                    .build();

            groupChatUserJoinRepository.save(join);
        }
    }

    @Override
    public void addUsers(GroupChat chat, AccountInfo author, List<AccountInfo> toBeAdded) {
        if (groupChatRepository.findGroupChatById(chat.getId()) == null) {
            throw new BadRequestException("This chat does not exist");
        }

        if (!userMemberOfGroupChat(chat, author)) {
            throw new UnauthorizedException("This user cannot add users to this chat");
        }

        uncheckedAddUsers(chat, author, toBeAdded);
    }

    // utility function to add users after all checks
    private void uncheckedAddUsers(
            GroupChat chat,
            AccountInfo author,
            List<AccountInfo> toBeAdded
    ) {
        Set<GroupChatUser> allUsersJoin = toBeAdded.stream()
                .filter(user -> !userMemberOfGroupChat(chat, user))
                .map(user -> {
                    MemberType memberType = MemberType.readwrite;
                    if (user.equals(author)) {
                        memberType = MemberType.admin;
                    }

                    return GroupChatUser.builder()
                            .id(new GroupChatUserId(chat.getId(), user.getId()))
                            .memberType(memberType)
                            .build();
                }).collect(Collectors.toSet());

        groupChatUserJoinRepository.saveAll(allUsersJoin);
    }

    @Override
    public void deleteUser(GroupChat chat, AccountInfo author, AccountInfo toBeDeleted) {
        GroupChatUser authorJoin = groupChatUserJoinRepository
                .findByGroupChatIdAndUserId(chat.getId(), author.getId());
        GroupChatUser toBeDeletedJoin = groupChatUserJoinRepository
                .findByGroupChatIdAndUserId(chat.getId(), author.getId());

        if (!userMemberOfGroupChat(chat, author) &&
                authorJoin.getMemberType() != MemberType.admin) {
            throw new UnauthorizedException("This user cannot delete members of this chat");
        }

        if (chat.getCreator().equals(toBeDeleted)) {
            throw new UnauthorizedException("This user cannot delete chat creator");
        }

        groupChatUserJoinRepository.delete(toBeDeletedJoin);
    }

    @Override
    public void changeUserMemberType(
            GroupChat chat,
            AccountInfo author,
            AccountInfo member,
            MemberType newMemberType
    ) {
        throw new UnimplementedException();
    }

    @Override
    public List<GroupChat> getAllGroupChatsForUser(AccountInfo user) {
        List<GroupChatUser> joins = groupChatUserJoinRepository.findAllByUserId(user.getId());
        List<UUID> chatsIds = joins.stream()
                .map(join -> join.getId().getGroupChatId())
                .collect(Collectors.toList());
        return groupChatRepository.findAllByIdIn(chatsIds);
    }

    @Override
    public List<GroupChat> getGroupChatsPageForUser(AccountInfo user) {
        throw new UnimplementedException();
    }
}