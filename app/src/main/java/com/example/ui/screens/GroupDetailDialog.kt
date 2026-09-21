package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.GroupEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeBackground
import com.example.ui.theme.KundeBorder
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeGoldDark
import com.example.ui.theme.KundeGreen
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyCard
import com.example.ui.theme.KundeNavyDark
import com.example.ui.theme.KundeSurface
import com.example.ui.theme.KundeTextMuted
import com.example.ui.theme.KundeTextPrimary
import com.example.ui.theme.KundeTextSecondary

data class GroupMember(
    val name: String,
    val role: String,
    val location: String,
    val badge: String
)

@Composable
fun GroupDetailDialog(
    group: GroupEntity,
    language: Language,
    onDismiss: () -> Unit,
    onToggleJoin: () -> Unit,
    onOpenGroupChat: () -> Unit
) {
    val members = listOf(
        GroupMember("Josué Kunde", "Fondateur Familia", "Kinshasa", "🇨🇩"),
        GroupMember("Grâce Mwamba", "Coordinatrice Terrain", "Bunia • Ituri", "🕊️"),
        GroupMember("Pasteur Emmanuel Makiese", "Conseiller Spirituel", "Kinshasa", "🙏"),
        GroupMember("Maman Marie Claire", "Déléguée Solidarité", "Lubumbashi", "🤝"),
        GroupMember("Patrick Kanyinda", "Logistique & Secours", "Goma", "📦"),
        GroupMember("Gloire Ilunga", "Jeunesse Unie", "Ituri", "⭐")
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .testTag("group_detail_dialog"),
            colors = CardDefaults.cardColors(containerColor = KundeSurface),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, KundeBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(KundeGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Group",
                                tint = KundeNavyDark,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "COMMUNAUTÉ FAMILIA",
                            color = KundeGoldDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = KundeTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title & Description
                Text(
                    text = group.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = KundeNavy
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = KundeGoldDark,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = group.location,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = KundeNavy
                    )
                    Text(
                        text = " • ${group.memberCount} " + AppStrings.get("members_count", language),
                        fontSize = 12.sp,
                        color = KundeTextSecondary
                    )
                }

                Text(
                    text = group.description,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = KundeTextSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Action row: Join/Leave and Chat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onToggleJoin,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (group.isJoined) KundeGreen else KundeGold,
                            contentColor = if (group.isJoined) Color.White else KundeNavyDark
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = if (group.isJoined) Icons.Default.Check else Icons.Default.GroupAdd,
                            contentDescription = "Join",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (group.isJoined) "Membre actif" else "Rejoindre le groupe",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = onOpenGroupChat,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = KundeNavy,
                            contentColor = KundeGold
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Discuter",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Member List Section
                Text(
                    text = "MEMBRES DE LA COORDINATION & SOLIDARITÉ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = KundeGoldDark,
                    letterSpacing = 0.8.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(members) { m ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(KundeBackground)
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(KundeNavy),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = m.name.take(1),
                                    color = KundeGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = m.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = KundeTextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = m.badge, fontSize = 11.sp)
                                }
                                Text(
                                    text = "${m.role} • ${m.location}",
                                    fontSize = 11.sp,
                                    color = KundeTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
