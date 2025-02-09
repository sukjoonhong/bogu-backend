package com.bogu.domain.entity.postgresql

import com.bogu.domain.ChatRoomType
import jakarta.persistence.*

@Entity
@Table(name = "chat_room")
data class ChatRoom(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val name: String? = null,

    val deleted: Boolean = false,

    @Enumerated(EnumType.STRING)
    val type: ChatRoomType = ChatRoomType.DIRECT,
) : BaseEntity() {

    @OneToMany(
        mappedBy = "chatRoom",
        fetch = FetchType.LAZY,
    )
    val members: Set<ChatRoomMember> = emptySet()

    @OneToMany(
        mappedBy = "chatRoom",
        fetch = FetchType.LAZY
    )
    val chatMessages: Set<ChatMessage> = emptySet()
}