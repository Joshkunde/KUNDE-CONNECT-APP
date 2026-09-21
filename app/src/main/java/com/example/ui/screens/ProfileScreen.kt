package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.PostEntity
import com.example.data.local.entity.UserEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeBackground
import com.example.ui.theme.KundeBorder
import com.example.ui.theme.KundeCongoRed
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeGoldDark
import com.example.ui.theme.KundeGreen
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyCard
import com.example.ui.theme.KundeNavyDark
import com.example.ui.theme.KundeNavyLight
import com.example.ui.theme.KundeSurface
import com.example.ui.theme.KundeTextMuted
import com.example.ui.theme.KundeTextPrimary
import com.example.ui.theme.KundeTextSecondary

@Composable
fun ProfileScreen(
    user: UserEntity?,
    userPosts: List<PostEntity>,
    language: Language,
    isDataSaver: Boolean,
    isOnline: Boolean,
    onToggleDataSaver: () -> Unit,
    onToggleOnline: () -> Unit,
    onOpenLanguage: () -> Unit,
    onOpenAuth: () -> Unit,
    onUpdateBio: (String) -> Unit,
    onLikePost: (PostEntity) -> Unit,
    onCommentPost: (PostEntity) -> Unit,
    onSharePost: (PostEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditingBio by remember { mutableStateOf(false) }
    var editedBio by remember(user?.bio) { mutableStateOf(user?.bio ?: "") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KundeBackground)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Profile Header Banner with African Gold & Congo Navy and Overlapping Profile Card
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(KundeNavyDark, KundeNavy, KundeNavyLight)
                            )
                        )
                ) {
                    // African pattern accents
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "🇨🇩 RÉPUBLIQUE DÉMOCRATIQUE DU CONGO",
                            color = KundeGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "FAMILIA",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Profile Avatar & Identity Card (safely overlapping the banner)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 90.dp),
                    colors = CardDefaults.cardColors(containerColor = KundeSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, KundeBorder)
                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar with Gold Border
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(KundeNavyDark)
                            .border(3.dp, KundeGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user?.name?.take(2)?.uppercase() ?: "JK",
                            color = KundeGold,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = user?.name ?: "Josué Kunde",
                            color = KundeTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = KundeGoldDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = user?.handle ?: "@josue_kunde",
                        color = KundeTextMuted,
                        fontSize = 13.sp
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = KundeCongoRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = user?.location ?: "Kinshasa, RDC 🇨🇩",
                            color = KundeTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bio display / Edit
                    if (isEditingBio) {
                        OutlinedTextField(
                            value = editedBio,
                            onValueChange = { editedBio = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("edit_bio_input"),
                            label = { Text("Modifier votre bio") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    onUpdateBio(editedBio)
                                    isEditingBio = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = KundeNavy)
                            ) {
                                Text("Enregistrer", color = KundeGold)
                            }
                            OutlinedButton(onClick = { isEditingBio = false }) {
                                Text("Annuler")
                            }
                        }
                    } else {
                        Text(
                            text = user?.bio ?: "Fils du Congo • Engagé pour la Foi, la Famille et la Paix en Ituri 🇨🇩",
                            color = KundeTextPrimary,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { isEditingBio = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = KundeNavy.copy(alpha = 0.08f),
                                contentColor = KundeNavy
                            ),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("edit_profile_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.get("edit_profile", language),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Community Stats (Abonnés, Abonnements, Publications)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(KundeBackground)
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${user?.followersCount ?: 1420}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = KundeNavy
                            )
                            Text(
                                text = AppStrings.get("followers", language),
                                fontSize = 11.sp,
                                color = KundeTextSecondary
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${user?.followingCount ?: 380}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = KundeNavy
                            )
                            Text(
                                text = AppStrings.get("following", language),
                                fontSize = 11.sp,
                                color = KundeTextSecondary
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${userPosts.size.coerceAtLeast(4)}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = KundeNavy
                            )
                            Text(
                                text = AppStrings.get("posts_count", language),
                                fontSize = 11.sp,
                                color = KundeTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }

        // Settings & Connectivity Section (Afrique & 2G/3G optimization)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "PARAMÈTRES & OPTIMISATION AFRIQUE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeGoldDark,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = KundeSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, KundeBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Data saver toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(KundeNavy.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CellTower,
                                        contentDescription = "Data Saver",
                                        tint = KundeNavy,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = AppStrings.get("data_saver", language),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = KundeTextPrimary
                                    )
                                    Text(
                                        text = AppStrings.get("data_saver_desc", language),
                                        fontSize = 11.sp,
                                        color = KundeTextMuted
                                    )
                                }
                            }
                            Switch(
                                checked = isDataSaver,
                                onCheckedChange = { onToggleDataSaver() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = KundeGold,
                                    checkedTrackColor = KundeNavy
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Offline sync simulation toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isOnline) KundeGreen.copy(alpha = 0.12f) else KundeCongoRed.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                                        contentDescription = "Network",
                                        tint = if (isOnline) KundeGreen else KundeCongoRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isOnline) AppStrings.get("network_status_online", language)
                                               else AppStrings.get("network_status_offline", language),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = KundeTextPrimary
                                    )
                                    Text(
                                        text = "Base Room locale • SQLite (< 20 Mo)",
                                        fontSize = 11.sp,
                                        color = KundeTextMuted
                                    )
                                }
                            }
                            Switch(
                                checked = isOnline,
                                onCheckedChange = { onToggleOnline() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = KundeGreen,
                                    checkedTrackColor = KundeNavy
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Language selector row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onOpenLanguage),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(KundeGold.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = "Language",
                                        tint = KundeGoldDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = AppStrings.get("switch_language", language),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = KundeTextPrimary
                                    )
                                    Text(
                                        text = "${language.flag} ${language.displayName}",
                                        fontSize = 11.sp,
                                        color = KundeTextMuted
                                    )
                                }
                            }

                            Text(
                                text = "Changer",
                                color = KundeNavy,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Auth / Account status row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = onOpenAuth),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(KundeNavyDark.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Auth",
                                        tint = KundeNavy,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Authentification Supabase / Email",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = KundeTextPrimary
                                    )
                                    Text(
                                        text = "Connecté en tant que ${user?.name ?: "Josué Kunde"}",
                                        fontSize = 11.sp,
                                        color = KundeGreen
                                    )
                                }
                            }

                            Text(
                                text = "Gérer",
                                color = KundeNavy,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Publications de l'utilisateur
        item {
            Text(
                text = "MES PUBLICATIONS DANS LA FAMILIA (${userPosts.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = KundeTextSecondary,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 6.dp)
            )
        }

        // User posts list
        items(userPosts, key = { it.id }) { post ->
            PostItemCard(
                post = post,
                language = language,
                isDataSaver = isDataSaver,
                onLike = { onLikePost(post) },
                onComment = { onCommentPost(post) },
                onShare = { onSharePost(post) },
                onGroupClick = {}
            )
        }
    }
}
