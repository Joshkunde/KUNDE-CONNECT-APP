package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.entity.GroupEntity
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeBackground
import com.example.ui.theme.KundeBorder
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeGoldDark
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyDark
import com.example.ui.theme.KundeNavyLight
import com.example.ui.theme.KundeSurface
import com.example.ui.theme.KundeTextMuted
import com.example.ui.theme.KundeTextPrimary
import com.example.ui.theme.KundeTextSecondary

data class CuratedImage(val title: String, val url: String, val tag: String)

@Composable
fun CreatePostDialog(
    groups: List<GroupEntity>,
    language: Language,
    onDismiss: () -> Unit,
    onSubmit: (content: String, category: String, groupTag: String?, mediaUrl: String?) -> Unit
) {
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("FOI") }
    var selectedGroupTag by remember { mutableStateOf<String?>(null) }
    var selectedMediaUrl by remember { mutableStateOf<String?>(null) }

    val samplePhotos = listOf(
        CuratedImage("Solidarité", "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&w=600&q=80", "Don / Paix"),
        CuratedImage("Kinshasa", "https://images.unsplash.com/photo-1547471080-7cc2caa01a7e?auto=format&fit=crop&w=600&q=80", "Congo"),
        CuratedImage("Famille", "https://images.unsplash.com/photo-1511895426328-dc8714191300?auto=format&fit=crop&w=600&q=80", "Unité"),
        CuratedImage("Prière", "https://images.unsplash.com/photo-1507692049790-de58290a4334?auto=format&fit=crop&w=600&q=80", "Foi")
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 20.dp)
                .testTag("create_post_dialog"),
            colors = CardDefaults.cardColors(containerColor = KundeSurface),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, KundeBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.get("create_post_title", language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = KundeNavy
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = KundeTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Post Content Input
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = {
                        Text(
                            text = AppStrings.get("create_post_hint", language),
                            fontSize = 14.sp,
                            color = KundeTextMuted
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .testTag("post_content_input"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = KundeNavy,
                        unfocusedBorderColor = KundeBorder,
                        focusedContainerColor = KundeBackground,
                        unfocusedContainerColor = KundeBackground
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category selector
                Text(
                    text = AppStrings.get("select_category", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeTextSecondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("FOI", "FAMILLE", "COMMUNAUTE").forEach { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KundeNavy,
                                selectedLabelColor = KundeGold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Mention / Link Group
                Text(
                    text = AppStrings.get("mention_group", language),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeTextSecondary
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedGroupTag == null,
                            onClick = { selectedGroupTag = null },
                            label = { Text(AppStrings.get("no_group", language), fontSize = 11.sp) }
                        )
                    }
                    items(groups) { grp ->
                        val isSelected = selectedGroupTag == grp.name
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedGroupTag = if (isSelected) null else grp.name },
                            label = { Text(grp.name.take(24) + "...", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = KundeNavyLight,
                                selectedLabelColor = KundeGold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Curated Photo attachment
                Text(
                    text = "Ajouter une image (optimisée)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = KundeTextSecondary
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(samplePhotos) { photo ->
                        val isSelected = selectedMediaUrl == photo.url
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) KundeGold else KundeNavy.copy(alpha = 0.08f))
                                .clickable {
                                    selectedMediaUrl = if (isSelected) null else photo.url
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = photo.tag,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) KundeNavyDark else KundeNavy
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Action Button
                Button(
                    onClick = {
                        onSubmit(content, selectedCategory, selectedGroupTag, selectedMediaUrl)
                    },
                    enabled = content.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_post_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = KundeNavy,
                        contentColor = KundeGold
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = AppStrings.get("post_btn", language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
