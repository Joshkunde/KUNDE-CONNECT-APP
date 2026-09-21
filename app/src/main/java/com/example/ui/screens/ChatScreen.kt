package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.MessageEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeBackground
import com.example.ui.theme.KundeBorder
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeGoldDark
import com.example.ui.theme.KundeGreen
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyDark
import com.example.ui.theme.KundeNavyLight
import com.example.ui.theme.KundeSurface
import com.example.ui.theme.KundeTextMuted
import com.example.ui.theme.KundeTextPrimary
import com.example.ui.theme.KundeTextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    messages: List<MessageEntity>,
    activeConversationId: String,
    conversationTitle: String,
    language: Language,
    onSelectConversation: (String, String, String) -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Controls whether we are in the WhatsApp chat list, settings, or inside a specific discussion
    var openedChatId by remember { mutableStateOf<String?>(null) }
    var showSettings by remember { mutableStateOf(false) }
    var currentChatTitle by remember { mutableStateOf(conversationTitle) }
    var currentChatAvatar by remember { mutableStateOf<String?>(null) }
    var currentChatInitials by remember { mutableStateOf("KC") }
    var currentChatColor by remember { mutableStateOf(Color(0xFF0B1F3A)) }

    // Active Call State
    var activeCallChannel by remember { mutableStateOf<String?>(null) }
    var isCallVideo by remember { mutableStateOf(true) }

    // If activeConversationId changes externally (e.g., from Group detail screen)
    LaunchedEffect(activeConversationId) {
        if (activeConversationId.isNotBlank() && activeConversationId != "conv_ituri") {
            openedChatId = activeConversationId
            currentChatTitle = conversationTitle
        }
    }

    if (activeCallChannel != null) {
        CallScreen(
            channelName = activeCallChannel ?: "Appel",
            isVideo = isCallVideo,
            onEndCall = { activeCallChannel = null },
            modifier = modifier
        )
    } else if (showSettings) {
        // Settings Screen as requested
        SettingsScreen(
            onBack = { showSettings = false },
            modifier = modifier
        )
    } else if (openedChatId == null) {
        // WhatsApp ChatListScreen view
        ChatListScreen(
            onSelectChat = { chat ->
                openedChatId = chat.id
                currentChatTitle = chat.name
                currentChatAvatar = chat.img
                currentChatInitials = chat.initials
                currentChatColor = chat.color
                onSelectConversation(chat.id, chat.name, chat.img ?: "")
            },
            onOpenSettings = { showSettings = true },
            onStartCall = { channel, isVideo ->
                activeCallChannel = channel
                isCallVideo = isVideo
            },
            modifier = modifier
        )
    } else {
        // Conversation Detail View
        ChatDetailScreen(
            messages = messages,
            chatTitle = currentChatTitle.ifBlank { conversationTitle },
            chatAvatar = currentChatAvatar,
            chatInitials = currentChatInitials,
            chatColor = currentChatColor,
            language = language,
            onBack = { openedChatId = null },
            onSendMessage = onSendMessage,
            onStartCall = { channel, isVideo ->
                activeCallChannel = channel
                isCallVideo = isVideo
            },
            modifier = modifier
        )
    }
}

@Composable
fun ChatDetailScreen(
    messages: List<MessageEntity>,
    chatTitle: String,
    chatAvatar: String?,
    chatInitials: String,
    chatColor: Color,
    language: Language,
    onBack: () -> Unit,
    onSendMessage: (String) -> Unit,
    onStartCall: (String, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var messageInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val quickReactions = listOf("🙏 Amen", "🇨🇩 RDC", "🕊️ Paix", "❤️ Bolingo", "✌️ Biso")

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KundeBackground)
            .testTag("chat_detail_screen")
    ) {
        // WhatsApp style TopBar for Conversation
        Surface(
            color = Color(0xFF0B1F3A),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("chat_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = Color.White
                    )
                }

                // Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(chatColor),
                    contentAlignment = Alignment.Center
                ) {
                    if (!chatAvatar.isNullOrBlank()) {
                        AsyncImage(
                            model = chatAvatar,
                            contentDescription = chatTitle,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = chatInitials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = chatTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(KundeGreen)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "En ligne • Chiffré de bout en bout",
                            color = KundeGold,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = {
                        val channel = chatTitle.replace(" ", "_")
                        onStartCall(channel, true)
                    },
                    modifier = Modifier.testTag("chat_detail_btn_videocam")
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Appel vidéo",
                        tint = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        val channel = chatTitle.replace(" ", "_")
                        onStartCall(channel, false)
                    },
                    modifier = Modifier.testTag("chat_detail_btn_phone")
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Appel vocal",
                        tint = Color.White
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = Color.White
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("chat_messages_list"),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(message = msg)
            }
        }

        // Quick Reactions bar
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(KundeSurface)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(quickReactions) { reaction ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(KundeNavy.copy(alpha = 0.08f))
                        .clickable { onSendMessage(reaction) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = reaction,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = KundeNavy
                    )
                }
            }
        }

        // Bottom Message Input Bar (WhatsApp style)
        Surface(
            color = KundeSurface,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 75.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageInput,
                    onValueChange = { messageInput = it },
                    placeholder = {
                        Text(
                            text = AppStrings.get("type_message_hint", language),
                            fontSize = 13.sp,
                            color = KundeTextMuted
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KundeGold,
                        unfocusedBorderColor = KundeBorder,
                        focusedContainerColor = KundeBackground,
                        unfocusedContainerColor = KundeBackground
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                onSendMessage("🎙️ [Note vocale 0:14 envoyée]")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Note vocale",
                                tint = KundeNavy
                            )
                        }
                    },
                    singleLine = false,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button in Gold
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700))
                        .clickable {
                            if (messageInput.isNotBlank()) {
                                onSendMessage(messageInput)
                                messageInput = ""
                            }
                        }
                        .testTag("send_message_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Envoyer",
                        tint = Color(0xFF0B1F3A),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: MessageEntity,
    modifier: Modifier = Modifier
) {
    val isMe = message.isFromMe
    val timeFormatter = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val timeString = timeFormatter.format(Date(message.timestamp))

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isMe) KundeNavy else KundeSurface
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isMe) 16.dp else 4.dp,
                bottomEnd = if (isMe) 4.dp else 16.dp
            ),
            border = if (isMe) null else androidx.compose.foundation.BorderStroke(1.dp, KundeBorder),
            modifier = Modifier
                .width(280.dp)
                .testTag("message_bubble_${message.id}")
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (!isMe) {
                    Text(
                        text = message.senderName,
                        color = KundeGoldDark,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }

                Text(
                    text = message.content,
                    color = if (isMe) Color.White else KundeTextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeString,
                        color = if (isMe) KundeGold.copy(alpha = 0.8f) else KundeTextMuted,
                        fontSize = 10.sp
                    )
                    if (isMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Lu",
                            tint = KundeGold,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }
        }
    }
}
