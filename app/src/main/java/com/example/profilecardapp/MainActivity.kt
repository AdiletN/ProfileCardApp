package com.example.profilecardapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
val Dark = Color(0xFF000000)
val GoldYellow = Color(0xFFFFD700)

fun openExternalLink(context: android.content.Context, uri: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

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

    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableStateOf(100) }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }

    val targetButtonColor = if (isFollowing) DarkPurple else GoldYellow
    val animatedButtonColor by animateColorAsState(
        targetValue = targetButtonColor,
        animationSpec = tween(durationMillis = 400),
        label = "Button Color Animation"
    )

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
                            color = Dark
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LightPurple,
                    titleContentColor = DarkPurple
                ),
                modifier = Modifier.shadow(6.dp)
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightPurple)
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
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
                        val image = painterResource(id = R.drawable.profile_image)
                        Image(
                            painter = image,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
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
                        text = "Java developer - Beginner",
                        style = TextStyle(
                            fontFamily = Lato,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = DarkPurple.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Followers: $animatedCount",
                        style = TextStyle(
                            fontFamily = Lato,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkPurple
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ElevatedButton(
                            onClick = {
                                if (!isFollowing) {
                                    isFollowing = true
                                    followerCount += 1
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "You followed Adilet",
                                            actionLabel = "Dismiss"
                                        )
                                    }
                                } else {
                                    showUnfollowDialog = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = animatedButtonColor),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = if (isFollowing) "Unfollow" else "Follow",
                                style = TextStyle(
                                    fontFamily = Lato,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = buttonTextColor
                                )
                            )
                        }

                        ElevatedButton(
                            onClick = {
                                openExternalLink(context, "mailto:230107139@sdu.edu.kz")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = DarkPurple
                            ),
                            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = "Message",
                                style = TextStyle(
                                    fontFamily = Lato,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = {
                            openExternalLink(context, "https://www.instagram.com/naurzalin_adilet/")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_instagram),
                                contentDescription = "Instagram",
                                tint = DarkPurple,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        IconButton(onClick = {
                            openExternalLink(context, "https://github.com/AdiletN")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_github),
                                contentDescription = "GitHub",
                                tint = DarkPurple,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        IconButton(onClick = {
                            openExternalLink(context, "mailto:230107139@sdu.edu.kz")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = "Email",
                                tint = DarkPurple,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }
            }

            if (showUnfollowDialog) {
                AlertDialog(
                    onDismissRequest = { showUnfollowDialog = false },
                    title = { Text(text = "Unfollow?") },
                    text = { Text(text = "Are you sure you want to unfollow Adilet?") },
                    confirmButton = {
                        TextButton(onClick = {
                            if (followerCount > 0) followerCount -= 1
                            isFollowing = false
                            showUnfollowDialog = false
                            scope.launch {
                                snackbarHostState.showSnackbar("You unfollowed Adilet")
                            }
                        }) {
                            Text(text = "Unfollow", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showUnfollowDialog = false }) {
                            Text(text = "Cancel")
                        }
                    }
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
