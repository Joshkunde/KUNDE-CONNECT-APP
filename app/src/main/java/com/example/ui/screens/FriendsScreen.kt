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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.local.entity.FriendEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeBackground
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyDark
import com.example.ui.theme.KundeSurface
import com.example.ui.theme.KundeTextMuted
import com.example.ui.theme.KundeTextPrimary

/**
 * FriendsScreen matching the user's Flutter request:
 * AppBar with "Amis", search icon, requests list with "Ajouter" and "Supprimer" buttons,
 * mutual friends count, and user avatars.
 */
@Composable
fun FriendsScreen(
    friends: List<FriendEntity>,
    language: Language,
    onAcceptFriend: (String) -> Unit,
    onDeleteFriend: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingRequests = friends.filter { it.status == "pending" }
    val acceptedFriends = friends.filter { it.status == "accepted" }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KundeBackground)
            .testTag("friends_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Header
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                colors = CardDefaults.cardColors(containerColor = KundeNavy),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = AppStrings.get("tab_friends", language),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(KundeGold)
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${pendingRequests.size} demandes",
                                    color = KundeNavyDark,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = "Connectez-vous avec vos proches et la communauté de l'Ituri",
                            fontSize = 12.sp,
                            color = KundeGold.copy(alpha = 0.9f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search friends",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Section: Demandes d'amis (Pending)
        if (pendingRequests.isNotEmpty()) {
            item {
                Text(
                    text = "Demandes d'amis (${pendingRequests.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeNavy,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(pendingRequests, key = { it.id }) { friend ->
                FriendRequestCard(
                    friend = friend,
                    language = language,
                    onAccept = { onAcceptFriend(friend.id) },
                    onDelete = { onDeleteFriend(friend.id) }
                )
            }
        }

        // Section: Amis de la communauté (Accepted)
        if (acceptedFriends.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Vos amis (${acceptedFriends.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeNavy,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(acceptedFriends, key = { it.id }) { friend ->
                AcceptedFriendCard(
                    friend = friend,
                    language = language
                )
            }
        }
    }
}

@Composable
private fun FriendRequestCard(
    friend: FriendEntity,
    language: Language,
    onAccept: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("friend_card_${friend.id}"),
        colors = CardDefaults.cardColors(containerColor = KundeSurface),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image
            if (friend.receiverAvatar.isNotBlank()) {
                AsyncImage(
                    model = friend.receiverAvatar,
                    contentDescription = friend.receiverName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(KundeNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = friend.receiverName.take(1),
                        color = KundeGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.receiverName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeTextPrimary
                )
                Text(
                    text = "${friend.mutualFriendsCount} ${AppStrings.get("mutual_friends", language)}",
                    fontSize = 12.sp,
                    color = KundeTextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onAccept,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KundeNavy,
                            contentColor = KundeGold
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .testTag("accept_friend_${friend.id}"),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = AppStrings.get("btn_add", language),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .testTag("delete_friend_${friend.id}"),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = AppStrings.get("btn_delete", language),
                            fontSize = 13.sp,
                            color = KundeTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AcceptedFriendCard(
    friend: FriendEntity,
    language: Language
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = KundeSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = friend.receiverAvatar,
                contentDescription = friend.receiverName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = friend.receiverName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeTextPrimary
                )
                Text(
                    text = "${friend.mutualFriendsCount} ${AppStrings.get("mutual_friends", language)}",
                    fontSize = 11.sp,
                    color = KundeTextMuted
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(KundeNavy.copy(alpha = 0.1f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Ami",
                        tint = KundeNavy,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Amis",
                        color = KundeNavy,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
