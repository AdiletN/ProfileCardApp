package com.example.profilecardapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.profilecardapp.ui.theme.ProfileCardAppTheme
import kotlinx.coroutines.launch

val Lato = FontFamily(
    Font(R.font.lato_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.lato_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.lato_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.lato_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.lato_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.lato_black, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.lato_blackitalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.lato_thin, FontWeight.Thin, FontStyle.Normal),
    Font(R.font.lato_thinitalic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.lato_italic, FontWeight.Normal, FontStyle.Italic)
)

val LightPurple = Color(0xFFCEB844)
val DarkPurple = Color(0xFF000000)
val GoldYellow = Color(0xFFFFD700)

fun openExternalLink(context: android.content.Context, uri: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

data class Follower(val name: String, val avatarRes: Int, val isFollowing: Boolean)
data class Story(val name: String, val avatarRes: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileCardAppTheme {
                ProfileCardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCardScreen() {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedStory by remember { mutableStateOf<Story?>(null) }
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableStateOf(100) }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }

    var followers by rememberSaveable {
        mutableStateOf(
            listOf(
                Follower("Aruzhan", R.drawable.profile_image2, false),
                Follower("Dias", R.drawable.profile_image3, false),
                Follower("Ayazhan", R.drawable.profile_image2, true)
            )
        )
    }
    var recentlyRemoved by remember { mutableStateOf<Follower?>(null) }

    val targetButtonColor = if (isFollowing) DarkPurple else GoldYellow
    val animatedButtonColor by animateColorAsState(targetValue = targetButtonColor, animationSpec = tween(400), label = "Button Color Animation")
    val buttonTextColor = if (isFollowing) Color.White else DarkPurple
    val animatedCount by animateIntAsState(targetValue = followerCount)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = TextStyle(
                            fontFamily = Lato,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkPurple
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = LightPurple),
                modifier = Modifier.shadow(6.dp)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightPurple)
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val stories = listOf(
                    Story("Aruzhan", R.drawable.profile_image2),
                    Story("Dias", R.drawable.profile_image3),
                    Story("Ayazhan", R.drawable.profile_image2)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(stories.size) { index ->
                        val story = stories[index]
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onTap = { selectedStory = story })
                            }
                        ) {
                            Image(
                                painter = painterResource(id = story.avatarRes),
                                contentDescription = story.name,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(3.dp, GoldYellow, CircleShape)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = story.name, fontFamily = Lato, fontSize = 14.sp, color = DarkPurple)
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp, horizontal = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(GoldYellow.copy(alpha = 0.2f))
                                .padding(6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.profile_image),
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Adilet Naurzalin",
                            style = TextStyle(
                                fontFamily = Lato,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkPurple
                            )
                        )
                        Text(
                            text = "Java Developer - Beginner",
                            style = TextStyle(fontFamily = Lato, fontSize = 16.sp, color = DarkPurple.copy(alpha = 0.6f))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Followers: $animatedCount",
                            style = TextStyle(fontFamily = Lato, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkPurple)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        ElevatedButton(
                            onClick = {
                                if (!isFollowing) {
                                    isFollowing = true
                                    followerCount++
                                    scope.launch { snackbarHostState.showSnackbar("You followed Adilet") }
                                } else showUnfollowDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = animatedButtonColor)
                        ) {
                            Text(
                                text = if (isFollowing) "Unfollow" else "Follow",
                                color = buttonTextColor,
                                fontFamily = Lato,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        ElevatedButton(
                            onClick = { openExternalLink(context, "mailto:230107139@sdu.edu.kz") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text(
                                text = "Message",
                                color = DarkPurple,
                                fontFamily = Lato,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Text(
                    text = "Followers List",
                    fontFamily = Lato,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkPurple,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(followers.size, key = { followers[it].name }) { index ->
                        val follower = followers[index]
                        SwipeToDismissFollower(
                            follower = follower,
                            onRemove = {
                                recentlyRemoved = follower
                                followers = followers - follower
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${follower.name} removed",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        followers = followers + follower
                                    }
                                }
                            },
                            onFollowToggle = {
                                followers = followers.map {
                                    if (it.name == follower.name) it.copy(isFollowing = !it.isFollowing) else it
                                }
                            }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = selectedStory != null,
                enter = fadeIn(tween(400)),
                exit = fadeOut(tween(400))
            ) {
                selectedStory?.let { story ->
                    StoryScreen(story = story, onClose = { selectedStory = null })
                }
            }

            if (showUnfollowDialog) {
                AlertDialog(
                    onDismissRequest = { showUnfollowDialog = false },
                    title = { Text("Unfollow?") },
                    text = { Text("Are you sure you want to unfollow Adilet?") },
                    confirmButton = {
                        TextButton(onClick = {
                            followerCount--
                            isFollowing = false
                            showUnfollowDialog = false
                            scope.launch { snackbarHostState.showSnackbar("You unfollowed Adilet") }
                        }) {
                            Text("Unfollow", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showUnfollowDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun StoryScreen(story: Story, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f))
            .pointerInput(Unit) { detectTapGestures(onTap = { onClose() }) },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = story.avatarRes),
                contentDescription = story.name,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(3.dp, GoldYellow, CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Привет 👋, я ${story.name}",
                color = Color.White,
                fontSize = 22.sp,
                fontFamily = Lato,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(40.dp))
            TextButton(onClick = onClose) {
                Text("Закрыть", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun SwipeToDismissFollower(
    follower: Follower,
    onRemove: () -> Unit,
    onFollowToggle: () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = offsetX.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (offsetX < -150f) onRemove()
                        offsetX = 0f
                    },
                    onDrag = { _, dragAmount -> offsetX += dragAmount.x }
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = follower.avatarRes),
                    contentDescription = follower.name,
                    modifier = Modifier.size(60.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = follower.name,
                    style = TextStyle(
                        fontFamily = Lato,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkPurple
                    )
                )
            }
            Button(
                onClick = { onFollowToggle() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (follower.isFollowing) DarkPurple else GoldYellow
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (follower.isFollowing) "Unfollow" else "Follow",
                    color = if (follower.isFollowing) Color.White else DarkPurple,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileCardAppTheme {
        ProfileCardScreen()
    }
}
