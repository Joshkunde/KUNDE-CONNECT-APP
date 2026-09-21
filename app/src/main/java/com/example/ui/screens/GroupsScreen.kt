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
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.GroupEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeBackground
import com.example.ui.theme.KundeBorder
import com.example.ui.theme.KundeCongoRed
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeGoldDark
import com.example.ui.theme.KundeGoldLight
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
fun GroupsScreen(
    groups: List<GroupEntity>,
    language: Language,
    onToggleJoinGroup: (GroupEntity) -> Unit,
    onOpenGroupDetail: (GroupEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredGroups = if (searchQuery.isBlank()) {
        groups
    } else {
        groups.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.description.contains(searchQuery, ignoreCase = true) ||
            it.location.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KundeBackground)
            .testTag("groups_screen_list"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Header & Search
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = KundeNavy),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(KundeGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Groups,
                                contentDescription = "Familia Groups",
                                tint = KundeNavyDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = AppStrings.get("groups_title", language),
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = AppStrings.get("groups_subtitle", language),
                                color = KundeGold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("group_search_input"),
                        placeholder = {
                            Text(
                                text = "Rechercher un groupe, une province...",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = KundeGold
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = KundeGold,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                            focusedContainerColor = KundeNavyDark.copy(alpha = 0.5f),
                            unfocusedContainerColor = KundeNavyDark.copy(alpha = 0.3f)
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        // Spotlight Section: "Ensemble pour l'Ituri"
        val ituriGroup = groups.find { it.id == "group_ituri" }
        if (ituriGroup != null && searchQuery.isBlank()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Text(
                        text = "GROUPE VEDETTE • SOLIDARITÉ RDC",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = KundeGoldDark,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    GroupCardItem(
                        group = ituriGroup,
                        isSpotlight = true,
                        language = language,
                        onToggleJoin = { onToggleJoinGroup(ituriGroup) },
                        onOpenDetail = { onOpenGroupDetail(ituriGroup) }
                    )
                }
            }
        }

        // Section Title: Tous les groupes
        item {
            Text(
                text = "TOUTES LES COMMUNAUTÉS FAMILIA (${filteredGroups.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = KundeTextSecondary,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 6.dp)
            )
        }

        // Groups list
        items(filteredGroups, key = { it.id }) { group ->
            if (group.id != "group_ituri" || searchQuery.isNotBlank()) {
                GroupCardItem(
                    group = group,
                    isSpotlight = false,
                    language = language,
                    onToggleJoin = { onToggleJoinGroup(group) },
                    onOpenDetail = { onOpenGroupDetail(group) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun GroupCardItem(
    group: GroupEntity,
    isSpotlight: Boolean,
    language: Language,
    onToggleJoin: () -> Unit,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onOpenDetail)
            .testTag("group_card_${group.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isSpotlight) KundeNavyCard else KundeSurface
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSpotlight) KundeGold.copy(alpha = 0.6f) else KundeBorder
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Group badge icon
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isSpotlight) KundeGold else KundeNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = group.name.take(2).uppercase(),
                        color = if (isSpotlight) KundeNavyDark else KundeGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isSpotlight) Color.White else KundeTextPrimary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = if (isSpotlight) KundeGold else KundeTextMuted,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = group.location,
                            fontSize = 11.sp,
                            color = if (isSpotlight) KundeGoldLight else KundeTextSecondary
                        )
                        Text(
                            text = " • ${group.memberCount} " + AppStrings.get("members_count", language),
                            fontSize = 11.sp,
                            color = if (isSpotlight) Color.White.copy(alpha = 0.7f) else KundeTextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = group.description,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                color = if (isSpotlight) Color.White.copy(alpha = 0.9f) else KundeTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSpotlight) Color.White.copy(alpha = 0.15f)
                            else KundeNavy.copy(alpha = 0.08f)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = group.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSpotlight) KundeGold else KundeNavy
                    )
                }

                // Join/Leave action button
                Button(
                    onClick = onToggleJoin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (group.isJoined) {
                            if (isSpotlight) Color.White.copy(alpha = 0.2f) else KundeGreen.copy(alpha = 0.15f)
                        } else {
                            KundeGold
                        },
                        contentColor = if (group.isJoined) {
                            if (isSpotlight) Color.White else KundeGreen
                        } else {
                            KundeNavyDark
                        }
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("join_btn_${group.id}")
                ) {
                    Icon(
                        imageVector = if (group.isJoined) Icons.Default.Check else Icons.Default.GroupAdd,
                        contentDescription = "Join",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (group.isJoined) AppStrings.get("joined_group", language)
                               else AppStrings.get("join_group", language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
