package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.ScreenShare
import androidx.compose.material.icons.filled.StopScreenShare
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

data class CallParticipant(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val isLocal: Boolean = false,
    val isSpeaking: Boolean = false,
    val isMuted: Boolean = false,
    val isVideoOff: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallScreen(
    channelName: String,
    isVideo: Boolean = true,
    onEndCall: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Call states
    var muted by remember { mutableStateOf(false) }
    var videoOff by remember { mutableStateOf(!isVideo) }
    var sharingScreen by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(true) }
    var isFrontCamera by remember { mutableStateOf(true) }
    var showAddPersonSheet by remember { mutableStateOf(false) }

    // Connected simulated / remote participants list
    val participants = remember {
        mutableStateListOf<CallParticipant>()
    }

    // Call timer in seconds
    var callDurationSeconds by remember { mutableLongStateOf(0L) }
    var callStatusText by remember { mutableStateOf("Connexion...") }

    // Permission handling (RECORD_AUDIO, CAMERA)
    var permissionsGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        permissionsGranted = perms[Manifest.permission.RECORD_AUDIO] == true &&
                perms[Manifest.permission.CAMERA] == true
    }

    LaunchedEffect(Unit) {
        if (!permissionsGranted) {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
            )
        }
    }

    // Simulate connection and user joining like Agora
    LaunchedEffect(channelName) {
        delay(1200)
        callStatusText = "Sonnerie..."
        delay(1800)
        callStatusText = "En communication"
        // Remote caller joins the channel
        participants.add(
            CallParticipant(
                id = "user_remote_1",
                name = channelName.replace('_', ' '),
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
                isLocal = false
            )
        )
    }

    // Call duration timer once connected
    LaunchedEffect(participants.size) {
        if (participants.isNotEmpty()) {
            while (true) {
                delay(1000)
                callDurationSeconds++
            }
        }
    }

    val formattedDuration = remember(callDurationSeconds) {
        val mins = callDurationSeconds / 60
        val secs = callDurationSeconds % 60
        String.format("%02d:%02d", mins, secs)
    }

    // Available family and contacts to add to call
    val contactsToAdd = remember {
        listOf(
            CallParticipant("p_1", "Maman Esther", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=400&auto=format&fit=crop&q=80"),
            CallParticipant("p_2", "Paul Kunde", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&auto=format&fit=crop&q=80"),
            CallParticipant("p_3", "Dr. David Kunde", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&auto=format&fit=crop&q=80"),
            CallParticipant("p_4", "Espoir Makisanza", "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=400&auto=format&fit=crop&q=80")
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0B1F3A))
            .testTag("call_screen")
    ) {
        // Top status / Channel info bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onEndCall,
                modifier = Modifier.testTag("call_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = Color.White
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = channelName.replace('_', ' '),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = if (participants.isNotEmpty()) formattedDuration else callStatusText,
                    color = if (participants.isNotEmpty()) Color(0xFFFFD700) else Color.LightGray,
                    fontSize = 13.sp
                )
            }

            // Flip camera if video is on
            IconButton(
                onClick = { isFrontCamera = !isFrontCamera },
                enabled = !videoOff
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Changer de caméra",
                    tint = if (!videoOff) Color.White else Color.Transparent
                )
            }
        }

        // Screen share indicator banner
        if (sharingScreen) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(top = 90.dp, start = 20.dp, end = 20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFD700).copy(alpha = 0.9f))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ScreenShare,
                        contentDescription = null,
                        tint = Color(0xFF0B1F3A),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Partage d'écran actif dans KUNDE CONNECT",
                        color = Color(0xFF0B1F3A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Main Video / Call Display (comme Facebook Messenger & WhatsApp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp, bottom = 120.dp),
            contentAlignment = Alignment.Center
        ) {
            if (participants.isEmpty()) {
                // Ringing state with avatar
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFFFFD700), CircleShape)
                            .background(Color(0xFF132B4F)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&auto=format&fit=crop&q=80",
                            contentDescription = "Avatar de l'appel",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Appel de ${channelName.replace('_', ' ')}",
                        style = androidx.compose.ui.text.TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = callStatusText,
                        style = androidx.compose.ui.text.TextStyle(
                            color = Color.Gray,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "KUNDE CONNECT • Réseau Sécurisé",
                        style = androidx.compose.ui.text.TextStyle(
                            color = Color(0xFFFFD700),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            } else {
                // GridView of Participants (Agora Video View representation)
                val allGridParticipants = buildList {
                    // Local user (index 0)
                    add(
                        CallParticipant(
                            id = "local_user",
                            name = "Vous",
                            avatarUrl = "",
                            isLocal = true,
                            isMuted = muted,
                            isVideoOff = videoOff
                        )
                    )
                    // Remote users
                    addAll(participants)
                }

                if (allGridParticipants.size <= 2) {
                    // 1-on-1 Full screen layout style WhatsApp/Messenger
                    val remote = allGridParticipants[1]
                    val local = allGridParticipants[0]

                    Box(modifier = Modifier.fillMaxSize()) {
                        // Remote participant full view
                        ParticipantVideoTile(
                            participant = remote,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )

                        // Local PIP floating in corner
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                                .size(width = 110.dp, height = 150.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(2.dp, Color(0xFFFFD700), RoundedCornerShape(12.dp))
                        ) {
                            ParticipantVideoTile(
                                participant = local,
                                isPip = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    // Multi-user Grid layout (GridView.builder style)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(allGridParticipants, key = { it.id }) { p ->
                            ParticipantVideoTile(
                                participant = p,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(0.85f)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                }
            }
        }

        // Bottom Action Bar style WhatsApp / Agora
        Surface(
            color = Color(0xFF0B1F3A).copy(alpha = 0.95f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mic button
                CallActionButton(
                    icon = if (muted) Icons.Default.MicOff else Icons.Default.Mic,
                    isActive = muted,
                    activeColor = Color.White,
                    activeIconColor = Color.Black,
                    inactiveColor = Color.White.copy(alpha = 0.2f),
                    contentDescription = if (muted) "Microphone coupé" else "Couper micro",
                    onClick = { muted = !muted },
                    testTag = "call_btn_mic"
                )

                // Video button
                CallActionButton(
                    icon = if (videoOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                    isActive = videoOff,
                    activeColor = Color.White,
                    activeIconColor = Color.Black,
                    inactiveColor = Color.White.copy(alpha = 0.2f),
                    contentDescription = if (videoOff) "Vidéo coupée" else "Couper vidéo",
                    onClick = { videoOff = !videoOff },
                    testTag = "call_btn_video"
                )

                // Screen share button
                CallActionButton(
                    icon = if (sharingScreen) Icons.Default.StopScreenShare else Icons.Default.ScreenShare,
                    isActive = sharingScreen,
                    activeColor = Color(0xFFFFD700),
                    activeIconColor = Color(0xFF0B1F3A),
                    inactiveColor = Color.White.copy(alpha = 0.2f),
                    contentDescription = "Partager écran",
                    onClick = { sharingScreen = !sharingScreen },
                    testTag = "call_btn_screen_share"
                )

                // Add person to call button
                CallActionButton(
                    icon = Icons.Default.PersonAdd,
                    isActive = false,
                    inactiveColor = Color.White.copy(alpha = 0.2f),
                    contentDescription = "Ajouter un participant",
                    onClick = { showAddPersonSheet = true },
                    testTag = "call_btn_add_person"
                )

                // End call button (Red)
                CallActionButton(
                    icon = Icons.Default.CallEnd,
                    isActive = false,
                    inactiveColor = Color(0xFFE53935),
                    inactiveIconColor = Color.White,
                    contentDescription = "Terminer l'appel",
                    onClick = onEndCall,
                    testTag = "call_btn_end"
                )
            }
        }

        // Add Person Modal Bottom Sheet
        if (showAddPersonSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddPersonSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .testTag("add_person_sheet")
                ) {
                    Text(
                        text = "Ajouter une personne à l'appel",
                        style = androidx.compose.ui.text.TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color(0xFF0B1F3A)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Réseau Familia KUNDE CONNECT (Ituri & Diaspora)",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    ) {
                        items(contactsToAdd, key = { it.id }) { contact ->
                            val isAlreadyInCall = participants.any { it.name == contact.name }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF0B1F3A)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AsyncImage(
                                        model = contact.avatarUrl,
                                        contentDescription = contact.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = contact.name,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 15.sp,
                                        color = Color(0xFF111111)
                                    )
                                    Text(
                                        text = "Disponible",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4CAF50)
                                    )
                                }

                                Button(
                                    onClick = {
                                        if (!isAlreadyInCall) {
                                            participants.add(contact)
                                            showAddPersonSheet = false
                                        }
                                    },
                                    enabled = !isAlreadyInCall,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF0B1F3A),
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.LightGray,
                                        disabledContentColor = Color.DarkGray
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.testTag("add_btn_${contact.id}")
                                ) {
                                    if (isAlreadyInCall) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Ajouté", fontSize = 12.sp)
                                    } else {
                                        Text("Ajouter", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun ParticipantVideoTile(
    participant: CallParticipant,
    modifier: Modifier = Modifier,
    isPip: Boolean = false
) {
    Box(
        modifier = modifier
            .background(Color(0xFF132B4F)),
        contentAlignment = Alignment.Center
    ) {
        if (!participant.isVideoOff && !participant.isLocal) {
            // Simulated active video feed (using realistic portrait backdrop with video filter)
            AsyncImage(
                model = participant.avatarUrl,
                contentDescription = participant.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            // Gradient overlay at bottom for name readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 300f
                        )
                    )
            )
        } else if (participant.isLocal && !participant.isVideoOff) {
            // Local preview box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(Color(0xFF1E3C72), Color(0xFF0B1F3A))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(if (isPip) 32.dp else 56.dp)
                    )
                    if (!isPip) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Caméra Locale Active",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        } else {
            // Video off state - Display avatar and status
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(if (isPip) 44.dp else 70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFD700)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF0B1F3A),
                        modifier = Modifier.size(if (isPip) 26.dp else 40.dp)
                    )
                }

                if (!isPip) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = participant.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Vidéo désactivée",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Bottom label with participant name & audio status
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(if (isPip) 4.dp else 10.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = participant.name,
                    color = Color.White,
                    fontSize = if (isPip) 10.sp else 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (participant.isMuted) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.MicOff,
                        contentDescription = "Micro coupé",
                        tint = Color.Red,
                        modifier = Modifier.size(if (isPip) 10.dp else 14.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CallActionButton(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.White,
    activeIconColor: Color = Color.Black,
    inactiveColor: Color = Color.White.copy(alpha = 0.2f),
    inactiveIconColor: Color = Color.White,
    testTag: String = ""
) {
    Box(
        modifier = modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(if (isActive) activeColor else inactiveColor)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isActive) activeIconColor else inactiveIconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}
