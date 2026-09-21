package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedInfoDialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showQrDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
            .testTag("settings_screen")
    ) {
        // AppBar style WhatsApp
        Surface(
            color = Color(0xFF0B1F3A),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("settings_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Retour",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Paramètres",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .testTag("settings_title")
                )
            }
        }

        // Body SingleChildScrollView
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            // Profil en haut comme WhatsApp
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showQrDialog = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("profile_tile"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // CircleAvatar(radius: 30, backgroundColor: Color(0xFFFFD700), child: Icon(Icons.person, size: 30, color: Color(0xFF0B1F3A)))
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFD700)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Photo de profil",
                            modifier = Modifier.size(32.dp),
                            tint = Color(0xFF0B1F3A)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "MAKISANZA KUNDE Josué",
                            style = androidx.compose.ui.text.TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF111111)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Familia en Ligne, Toujours Connecté\n+243 999 000 000",
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 13.sp,
                                color = Color(0xFF666666),
                                lineHeight = 18.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = { showQrDialog = true },
                        modifier = Modifier.testTag("qr_code_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Code QR",
                            tint = Color(0xFF0B1F3A),
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Section 1: Compte, Confidentialité, Avatar
            SettingsSection {
                SettingsTile(
                    icon = Icons.Default.Key,
                    title = "Compte",
                    subtitle = "Confidentialité, sécurité, numéro",
                    color = Color(0xFF2196F3),
                    onClick = {
                        selectedInfoDialog = "Compte" to "Gérez la sécurité de votre compte KUNDE CONNECT, vos vérifications en deux étapes et votre numéro de téléphone congolais (+243)."
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = Color(0xFFF0F0F0), thickness = 0.8.dp)
                SettingsTile(
                    icon = Icons.Default.Lock,
                    title = "Confidentialité",
                    subtitle = "Bloqués, dernière vue",
                    color = Color(0xFF448AFF),
                    onClick = {
                        selectedInfoDialog = "Confidentialité" to "Contrôlez qui peut voir votre présence en ligne, vos statuts et vos contacts bloqués au sein du réseau Familia."
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = Color(0xFFF0F0F0), thickness = 0.8.dp)
                SettingsTile(
                    icon = Icons.Default.Face,
                    title = "Avatar",
                    subtitle = "Créer, modifier photo",
                    color = Color(0xFF4CAF50),
                    onClick = {
                        selectedInfoDialog = "Avatar" to "Personnalisez votre avatar aux couleurs d'Ituri et de la République Démocratique du Congo."
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Section 2: Discussions, Notifications, Stockage et données
            SettingsSection {
                SettingsTile(
                    icon = Icons.Default.Chat,
                    title = "Discussions",
                    subtitle = "Thème, fonds d'écran, historique",
                    color = Color(0xFF4CAF50),
                    onClick = {
                        selectedInfoDialog = "Discussions" to "Thème sombre/clair, fonds d'écran aux couleurs de l'Afrique et sauvegarde locale de vos messages."
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = Color(0xFFF0F0F0), thickness = 0.8.dp)
                SettingsTile(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Sonneries, vibrations",
                    color = Color(0xFFF44336),
                    onClick = {
                        selectedInfoDialog = "Notifications" to "Configurez les alertes sonores pour les groupes Familia Ituri et les messages individuels."
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = Color(0xFFF0F0F0), thickness = 0.8.dp)
                SettingsTile(
                    icon = Icons.Default.DataUsage,
                    title = "Stockage et données",
                    subtitle = "Réseau, téléchargement auto",
                    color = Color(0xFF4CAF50),
                    onClick = {
                        selectedInfoDialog = "Stockage et données" to "Mode économie de données activé : optimisation pour réseaux 2G/3G en RDC et compression intelligente des médias."
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Section 3: Langue de l'app, Aide, Inviter un ami
            SettingsSection {
                SettingsTile(
                    icon = Icons.Default.Language,
                    title = "Langue de l'app",
                    subtitle = "Français",
                    color = Color.Gray,
                    onClick = {
                        selectedInfoDialog = "Langue de l'app" to "Langues supportées : Français, Lingala (Biso na Biso), Swahili (Umoja wetu) et Anglais."
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = Color(0xFFF0F0F0), thickness = 0.8.dp)
                SettingsTile(
                    icon = Icons.Default.HelpOutline,
                    title = "Aide",
                    subtitle = "Centre d'aide, contactez-nous",
                    color = Color.Gray,
                    onClick = {
                        selectedInfoDialog = "Centre d'Aide" to "Support KUNDE CONNECT : contactez l'équipe technique à Bunia et Kinshasa pour toute assistance."
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp), color = Color(0xFFF0F0F0), thickness = 0.8.dp)
                SettingsTile(
                    icon = Icons.Default.Group,
                    title = "Inviter un ami",
                    subtitle = "",
                    color = Color.Gray,
                    onClick = {
                        selectedInfoDialog = "Inviter un ami" to "Partagez KUNDE CONNECT avec votre famille et vos proches en Ituri et partout dans le monde."
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Section 4: KUNDE Société
            SettingsSection {
                SettingsTile(
                    icon = Icons.Default.Business,
                    title = "KUNDE Société",
                    subtitle = "Info de l'entreprise",
                    color = Color(0xFF0B1F3A),
                    onClick = {
                        selectedInfoDialog = "KUNDE Société" to "Société KUNDE & Familia Technologies.\nFondée par Josué Kunde.\nMission : Connecter les communautés d'Ituri et de toute la RDC avec des solutions numériques locales souveraines et résilientes."
                    }
                )
            }

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "KUNDE CONNECT",
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "v1.0.0 - Made in Ituri par Josué Kunde\nFils de KIBONGA KUNDE Paul & ESTHER BINEGA",
                        textAlign = TextAlign.Center,
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 11.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        // Dialogue d'information sur le paramètre cliqué
        selectedInfoDialog?.let { (title, desc) ->
            AlertDialog(
                onDismissRequest = { selectedInfoDialog = null },
                title = {
                    Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFF0B1F3A))
                },
                text = {
                    Text(text = desc, color = Color(0xFF444444), fontSize = 14.sp)
                },
                confirmButton = {
                    TextButton(onClick = { selectedInfoDialog = null }) {
                        Text("OK", color = Color(0xFF0B1F3A), fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }

        // Dialogue QR Code WhatsApp style
        if (showQrDialog) {
            AlertDialog(
                onDismissRequest = { showQrDialog = false },
                title = {
                    Text(
                        text = "Code QR KUNDE CONNECT",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0B1F3A),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFD700)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF0B1F3A),
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "MAKISANZA KUNDE Josué",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF111111)
                        )
                        Text(
                            text = "+243 999 000 000",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFF5F5F5))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                tint = Color(0xFF0B1F3A),
                                modifier = Modifier.size(140.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Votre code QR est privé. Ne le partagez qu'avec des personnes de confiance.",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showQrDialog = false }) {
                        Text("Fermer", color = Color(0xFF0B1F3A))
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
private fun SettingsSection(
    content: @Composable () -> Unit
) {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            content()
        }
    }
}

@Composable
private fun SettingsTile(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("settings_tile_${title.lowercase().replace(" ", "_")}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111111)
                )
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }
}
