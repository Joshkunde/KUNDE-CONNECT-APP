package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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

data class ChatItem(
    val id: String,
    val name: String,
    val msg: String,
    val time: String,
    val unread: Int,
    val isGroup: Boolean,
    val initials: String,
    val color: Color = Color(0xFF0B1F3A),
    val img: String? = null
)

val defaultChats = listOf(
    ChatItem(
        id = "conv_ituri",
        name = "Coordination Ituri",
        msg = "Très bien par la grâce de Dieu! Les mamans...",
        time = "09:15",
        unread = 3,
        isGroup = true,
        initials = "IT",
        color = Color(0xFFFF9800)
    ),
    ChatItem(
        id = "conv_pasteur",
        name = "Pasteur Emmanuel",
        msg = "C'est un devoir pour nous tous...",
        time = "08:28",
        unread = 0,
        isGroup = false,
        initials = "PE",
        img = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=150&q=80"
    ),
    ChatItem(
        id = "conv_famille",
        name = "Famille Kunde",
        msg = "Papa Paul: Amen 🙏",
        time = "Hier",
        unread = 12,
        isGroup = true,
        initials = "FK",
        color = Color(0xFF0B1F3A)
    ),
    ChatItem(
        id = "conv_maman",
        name = "Maman Esther Binega",
        msg = "[Note vocale 0:14 envoyée]",
        time = "Hier",
        unread = 0,
        isGroup = false,
        initials = "EB",
        img = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=150&q=80"
    ),
    ChatItem(
        id = "conv_jeunesse",
        name = "Jeunesse Bira Ituri",
        msg = "Josué: On se voit à Bunia cet après-midi?",
        time = "Lundi",
        unread = 1,
        isGroup = true,
        initials = "JB",
        color = Color(0xFF4CAF50)
    ),
    ChatItem(
        id = "conv_paul",
        name = "Paul Kunde",
        msg = "Ok mon fils",
        time = "Lundi",
        unread = 0,
        isGroup = false,
        initials = "PK",
        img = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=150&q=80"
    )
)

@Composable
fun ChatListScreen(
    onSelectChat: (ChatItem) -> Unit,
    onOpenSettings: () -> Unit = {},
    onStartCall: (String, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Tous") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showNewChatDialog by remember { mutableStateOf(false) }
    var showNewContactDialog by remember { mutableStateOf(false) }
    var chatList by remember { mutableStateOf(defaultChats) }

    val filteredChats = chatList.filter { chat ->
        val matchesFilter = when (selectedFilter) {
            "Groupes" -> chat.isGroup
            "Non lus" -> chat.unread > 0
            else -> true
        }
        val matchesSearch = if (searchQuery.isBlank()) true else {
            chat.name.contains(searchQuery, ignoreCase = true) ||
                    chat.msg.contains(searchQuery, ignoreCase = true)
        }
        matchesFilter && matchesSearch
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag("chat_list_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // WhatsApp-style AppBar with 0xFF0B1F3A background
            Surface(
                color = Color(0xFF0B1F3A),
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "KUNDE CONNECT",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.testTag("chat_list_title")
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { isSearchActive = !isSearchActive },
                                modifier = Modifier.testTag("chat_search_button")
                            ) {
                                Icon(
                                    imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                    contentDescription = "Recherche",
                                    tint = Color.White
                                )
                            }

                            Box {
                                IconButton(
                                    onClick = { showMenu = true },
                                    modifier = Modifier.testTag("chat_menu_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "Options",
                                        tint = Color.White
                                    )
                                }

                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Nouveau groupe") },
                                        onClick = {
                                            showMenu = false
                                            showNewChatDialog = true
                                        },
                                        modifier = Modifier.testTag("menu_new_group")
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Nouveau contact") },
                                        onClick = {
                                            showMenu = false
                                            showNewContactDialog = true
                                        },
                                        modifier = Modifier.testTag("menu_new_contact")
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Paramètres") },
                                        onClick = {
                                            showMenu = false
                                            onOpenSettings()
                                        },
                                        modifier = Modifier.testTag("menu_settings")
                                    )
                                }
                            }
                        }
                    }

                    // Expandable search bar if search is active
                    if (isSearchActive) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Rechercher...", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .testTag("chat_search_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFFFFD700),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedContainerColor = Color(0xFF132B4F),
                                unfocusedContainerColor = Color(0xFF132B4F)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            // Filtres comme WhatsApp (Tous, Groupes, Non lus)
            ContainerFilterBar(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            // Liste verticale WhatsApp
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("chat_list_vertical")
            ) {
                items(filteredChats, key = { it.id }) { chat ->
                    ChatListItem(
                        chat = chat,
                        onClick = {
                            // Mark unread as 0 on click
                            chatList = chatList.map {
                                if (it.id == chat.id) it.copy(unread = 0) else it
                            }
                            onSelectChat(chat)
                        },
                        onVideoCall = {
                            val channelName = chat.name.replace(" ", "_")
                            onStartCall(channelName, true)
                        }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 75.dp),
                        thickness = 0.8.dp,
                        color = Color(0xFFEEEEEE)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        // Floating Action Button in Gold (0xFFFFD700) with Navy Chat Icon
        FloatingActionButton(
            onClick = { showNewChatDialog = true },
            containerColor = Color(0xFFFFD700),
            contentColor = Color(0xFF0B1F3A),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 85.dp, end = 16.dp)
                .testTag("chat_fab_new")
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = "Nouveau message",
                modifier = Modifier.size(24.dp)
            )
        }

        // New Chat Dialog
        if (showNewChatDialog) {
            NewChatDialog(
                chats = chatList,
                onDismiss = { showNewChatDialog = false },
                onSelectContact = { contact ->
                    showNewChatDialog = false
                    onSelectChat(contact)
                }
            )
        }

        // New Contact Dialog
        if (showNewContactDialog) {
            NewContactDialog(
                onDismiss = { showNewContactDialog = false },
                onAddContact = { name, phone ->
                    val initials = name.trim().split(" ")
                        .take(2)
                        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                        .joinToString("")
                        .ifEmpty { "K" }
                    val newChat = ChatItem(
                        id = "conv_${System.currentTimeMillis()}",
                        name = name,
                        msg = phone,
                        time = "À l'instant",
                        unread = 0,
                        isGroup = false,
                        initials = initials,
                        color = Color(0xFF0B1F3A)
                    )
                    chatList = listOf(newChat) + chatList
                    showNewContactDialog = false
                    onSelectChat(newChat)
                }
            )
        }
    }
}

@Composable
private fun ContainerFilterBar(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Surface(
        color = Color(0xFFF5F5F5),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipItem(
                label = "Tous",
                selected = selectedFilter == "Tous",
                onClick = { onFilterSelected("Tous") },
                testTag = "filter_chip_all"
            )
            FilterChipItem(
                label = "Groupes",
                selected = selectedFilter == "Groupes",
                onClick = { onFilterSelected("Groupes") },
                testTag = "filter_chip_groups"
            )
            FilterChipItem(
                label = "Non lus",
                selected = selectedFilter == "Non lus",
                onClick = { onFilterSelected("Non lus") },
                testTag = "filter_chip_unread"
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String = ""
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0xFF0B1F3A) else Color.White)
            .border(
                width = 1.dp,
                color = if (selected) Color(0xFF0B1F3A) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else Color.Black,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ChatListItem(
    chat: ChatItem,
    onClick: () -> Unit,
    onVideoCall: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("chat_item_${chat.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading CircleAvatar (radius: 26 -> size: 52dp)
        if (chat.isGroup) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(chat.color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.initials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                if (!chat.img.isNullOrBlank()) {
                    AsyncImage(
                        model = chat.img,
                        contentDescription = chat.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = chat.initials,
                        color = Color(0xFF0B1F3A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Center & Trailing Information
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Title Row (Name and Time)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF111111),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = chat.time,
                    fontSize = 12.sp,
                    color = if (chat.unread > 0) Color(0xFF25D366) else Color(0xFF888888),
                    fontWeight = if (chat.unread > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            // Subtitle Row (Group icon + Message + Unread Badge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chat.isGroup) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Groupe",
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = chat.msg,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = androidx.compose.ui.text.TextStyle(
                        color = Color(0xFF666666),
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.weight(1f)
                )

                if (chat.unread > 0) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF25D366))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${chat.unread}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onVideoCall,
            modifier = Modifier.testTag("chat_item_call_${chat.id}")
        ) {
            Icon(
                imageVector = Icons.Default.Videocam,
                contentDescription = "Appel vidéo",
                tint = Color(0xFF0B1F3A),
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun NewChatDialog(
    chats: List<ChatItem>,
    onDismiss: () -> Unit,
    onSelectContact: (ChatItem) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nouvelle discussion",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B1F3A)
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Sélectionnez un contact ou un groupe :",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(chats) { contact ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectContact(contact) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (contact.isGroup) contact.color else Color(0xFF0B1F3A)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = contact.initials,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = contact.name,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (contact.isGroup) "Groupe Familia" else "Contact",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fermer", color = Color(0xFF0B1F3A))
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun NewContactDialog(
    onDismiss: () -> Unit,
    onAddContact: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("+243 ") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nouveau contact",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B1F3A)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom complet") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("new_contact_name_input")
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Numéro de téléphone") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("new_contact_phone_input")
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank()) {
                        onAddContact(name, phone)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text("Enregistrer", color = Color(0xFF0B1F3A), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

