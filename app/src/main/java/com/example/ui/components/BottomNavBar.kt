package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStrings
import com.example.data.model.Language
import com.example.ui.theme.KundeGold
import com.example.ui.theme.KundeNavy
import com.example.ui.theme.KundeNavyDark

data class TabItem(
    val index: Int,
    val titleKey: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun KundeBottomBar(
    selectedIndex: Int,
    language: Language,
    onSelectTab: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // 5 items: Accueil (0), Amis (1), Reels (2), Marché (3), Profil (4)
    val tabs = listOf(
        TabItem(0, "tab_feed", Icons.Default.Home, "tab_home"),
        TabItem(1, "tab_friends", Icons.Default.People, "tab_friends"),
        TabItem(2, "tab_reels", Icons.Default.VideoLibrary, "tab_reels"),
        TabItem(3, "tab_market", Icons.Default.Store, "tab_market"),
        TabItem(4, "tab_profile", Icons.Default.Person, "tab_profile")
    )

    Surface(
        color = KundeNavy,
        shadowElevation = 8.dp,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val isSelected = selectedIndex == tab.index
                val title = AppStrings.get(tab.titleKey, language)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) KundeGold.copy(alpha = 0.18f) else Color.Transparent)
                        .clickable { onSelectTab(tab.index) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag(tab.tag),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = title,
                            tint = if (isSelected) KundeGold else Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = title,
                            color = if (isSelected) KundeGold else Color.White.copy(alpha = 0.6f),
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
