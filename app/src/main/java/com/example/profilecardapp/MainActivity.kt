package com.example.profilecardapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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


val LightPurple = Color(0xFFE6E0FF)
val DarkPurple = Color(0xFF3B3060)
val GoldYellow = Color(0xFFFFD700)

fun openExternalLink(context: android.content.Context, uri: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun ProfileCardScreen() {
    val context = LocalContext.current
    var isFollowing by rememberSaveable { mutableStateOf(false) }
    var followerCount by rememberSaveable { mutableStateOf(100) }

    val targetButtonColor = if (isFollowing) DarkPurple else GoldYellow
    val animatedButtonColor by animateColorAsState(
        targetValue = targetButtonColor,
        animationSpec = tween(durationMillis = 500),
        label = "Button Color Animation"
    )

    val buttonTextColor = if (isFollowing) Color.White else DarkPurple

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPurple),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(GoldYellow)
                        .padding(4.dp)
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

                Spacer(modifier = Modifier.height(24.dp))

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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Followers: $followerCount",
                    style = TextStyle(
                        fontFamily = Lato,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkPurple
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        isFollowing = !isFollowing
                        followerCount = if (isFollowing) followerCount + 1 else followerCount - 1
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = animatedButtonColor)
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

                Spacer(modifier = Modifier.height(32.dp))

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
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileCardAppTheme {
        ProfileCardScreen()
    }
}