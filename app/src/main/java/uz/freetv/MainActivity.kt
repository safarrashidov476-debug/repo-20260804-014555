package uz.freetv

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.SkipNext
import androidx.compose.material.icons.automirrored.filled.SkipPrevious
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OzbekTVApp()
                }
            }
        }
    }
}

@Composable
fun OzbekTVApp() {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var playlist by remember { mutableStateOf(Channels.list) }

    if (selectedIndex == null) {
        ChannelListScreen(
            onChannelClick = { channel, filtered ->
                playlist = filtered
                selectedIndex = filtered.indexOfFirst { it.id == channel.id }.coerceAtLeast(0)
            }
        )
    } else {
        val index = selectedIndex!!
        val channel = playlist.getOrNull(index) ?: Channels.list.first()
        PlayerScreen(
            channel = channel,
            hasPrevious = index > 0,
            hasNext = index < playlist.lastIndex,
            onPrevious = { if (index > 0) selectedIndex = index - 1 },
            onNext = { if (index < playlist.lastIndex) selectedIndex = index + 1 },
            onBack = { selectedIndex = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelListScreen(
    onChannelClick: (Channel, List<Channel>) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Barchasi") }
    var showSearch by remember { mutableStateOf(false) }

    val filtered = remember(searchQuery, selectedCategory) {
        Channels.list.filter { ch ->
            val matchSearch = searchQuery.isBlank() ||
                ch.name.contains(searchQuery, ignoreCase = true)
            val matchCategory = selectedCategory == "Barchasi" || ch.category == selectedCategory
            matchSearch && matchCategory
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (showSearch) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Kanal qidirish...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics { contentDescription = "Qidiruv" }
                        )
                    } else {
                        Text(
                            "O'zbek TV",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.semantics { heading() }
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showSearch = !showSearch
                            if (!showSearch) searchQuery = ""
                        },
                        modifier = Modifier.semantics {
                            contentDescription = if (showSearch) "Yopish" else "Qidiruv"
                        }
                    ) {
                        Icon(
                            imageVector = if (showSearch) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(Channels.categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) },
                        modifier = Modifier.semantics {
                            contentDescription = cat
                            role = Role.Tab
                        }
                    )
                }
            }

            Text(
                text = "${filtered.size} ta",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Kanal topilmadi")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filtered, key = { it.id }) { channel ->
                        ChannelItem(
                            channel = channel,
                            onClick = { onChannelClick(channel, filtered) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChannelItem(
    channel: Channel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = channel.name
                role = Role.Button
            }
            .clickable(onClick = onClick, role = Role.Button),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = channel.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = channel.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    channel: Channel,
    hasPrevious: Boolean,
    hasNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    DisposableEffect(Unit) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    var isBuffering by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var playerStatus by remember { mutableStateOf("Yuklanmoqda") }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    LaunchedEffect(channel.id) {
        hasError = false
        isBuffering = true
        playerStatus = "Yuklanmoqda"
        exoPlayer.stop()
        exoPlayer.setMediaItem(MediaItem.fromUri(channel.url))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                isBuffering = playbackState == Player.STATE_BUFFERING
                when (playbackState) {
                    Player.STATE_BUFFERING -> playerStatus = "Yuklanmoqda"
                    Player.STATE_READY -> {
                        hasError = false
                        playerStatus = if (exoPlayer.isPlaying) "Ijro etilmoqda" else "To'xtatilgan"
                    }
                    Player.STATE_ENDED -> playerStatus = "Tugadi"
                    Player.STATE_IDLE -> playerStatus = "Tayyor emas"
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                if (!hasError && !isBuffering) {
                    playerStatus = if (playing) "Ijro etilmoqda" else "To'xtatilgan"
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                hasError = true
                errorMessage = error.message ?: "Stream xatosi"
                playerStatus = "Xato"
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    fun retry() {
        hasError = false
        playerStatus = "Qayta urinilmoqda"
        exoPlayer.stop()
        exoPlayer.setMediaItem(MediaItem.fromUri(channel.url))
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            channel.name,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.semantics { heading() }
                        )
                        Text(
                            text = playerStatus,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.semantics {
                                liveRegion = androidx.compose.ui.semantics.LiveRegionMode.Polite
                                contentDescription = playerStatus
                            }
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.semantics { contentDescription = "Orqaga" }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (hasError) {
                        IconButton(
                            onClick = { retry() },
                            modifier = Modifier.semantics { contentDescription = "Qayta urinish" }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .semantics { contentDescription = channel.name }
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true
                            controllerShowTimeoutMs = 4000
                            setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
                            contentDescription = channel.name
                            ViewCompat.setAccessibilityDelegate(this, object : androidx.core.view.AccessibilityDelegateCompat() {
                                override fun onInitializeAccessibilityNodeInfo(
                                    host: View,
                                    info: AccessibilityNodeInfoCompat
                                ) {
                                    super.onInitializeAccessibilityNodeInfo(host, info)
                                    info.className = "android.widget.VideoView"
                                    info.contentDescription = host.contentDescription
                                }
                            })
                        }
                    },
                    update = { view ->
                        view.player = exoPlayer
                        view.contentDescription = channel.name
                    },
                    modifier = Modifier.fillMaxSize()
                )

                if (isBuffering && !hasError) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .semantics { contentDescription = "Yuklanmoqda" }
                    )
                }

                if (hasError) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Kanal ochilmadi",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { retry() },
                            modifier = Modifier.semantics {
                                contentDescription = "Qayta urinish"
                                role = Role.Button
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Qayta urinish")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = onPrevious,
                    enabled = hasPrevious,
                    modifier = Modifier.semantics {
                        contentDescription = "Oldingi kanal"
                        role = Role.Button
                    }
                ) {
                    Icon(Icons.AutoMirrored.Filled.SkipPrevious, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Oldingi")
                }
                FilledTonalButton(
                    onClick = onNext,
                    enabled = hasNext,
                    modifier = Modifier.semantics {
                        contentDescription = "Keyingi kanal"
                        role = Role.Button
                    }
                ) {
                    Text("Keyingi")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.SkipNext, contentDescription = null)
                }
            }
        }
    }
}
