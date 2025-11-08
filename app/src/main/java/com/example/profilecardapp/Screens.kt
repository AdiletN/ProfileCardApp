package com.example.profilecardapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.profilecardapp.model.Follower
import com.example.profilecardapp.model.Story
import com.example.profilecardapp.ProfileViewModel
import com.example.profilecardapp.ui.theme.*

@Composable
fun HomeScreen(onGoProfile: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightPurple),
        contentAlignment = Alignment.Center
    ) {
        ElevatedButton(
            onClick = onGoProfile,
            modifier = Modifier
                .padding(16.dp)
                .height(56.dp)
                .fillMaxWidth(0.6f)
        ) {
            Text(
                text = "Go to Profile",
                fontFamily = Lato,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = GoldYellow
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onEdit: () -> Unit) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedStory by remember { mutableStateOf<Story?>(null) }
    var showUnfollowDialog by rememberSaveable { mutableStateOf(false) }

    val animatedButtonColor by animateColorAsState(
        targetValue = if (viewModel.isFollowing) DarkPurple else GoldYellow,
        animationSpec = tween(400)
    )
    val buttonTextColor = if (viewModel.isFollowing) androidx.compose.ui.graphics.Color.White else DarkPurple
    val animatedFollowerCount by animateIntAsState(viewModel.followerCount)
    val stories = viewModel.stories

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
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEdit() }) {
                Text("Edit", fontFamily = Lato)
            }
        }
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
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(stories) { story ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures { selectedStory = story }
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
                            Text(
                                text = story.name,
                                fontFamily = Lato,
                                fontSize = 14.sp,
                                color = DarkPurple
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
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
                            text = viewModel.name,
                            style = TextStyle(
                                fontFamily = Lato,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkPurple
                            )
                        )
                        Text(
                            text = viewModel.bio,
                            style = TextStyle(
                                fontFamily = Lato,
                                fontSize = 16.sp,
                                color = DarkPurple.copy(alpha = 0.6f)
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Followers: $animatedFollowerCount",
                            style = TextStyle(
                                fontFamily = Lato,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkPurple
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        ElevatedButton(
                            onClick = {
                                if (!viewModel.isFollowing) {
                                    viewModel.toggleFollowMain()
                                    scope.launch {
                                        snackbarHostState.showSnackbar("You followed ${viewModel.name}")
                                    }
                                } else showUnfollowDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = animatedButtonColor)
                        ) {
                            Text(
                                text = if (viewModel.isFollowing) "Unfollow" else "Follow",
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
                            colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.White)
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
                    items(items = viewModel.followers, key = { it.name }) { follower ->
                        SwipeToDismissFollower(
                            follower = follower,
                            onRemove = {
                                viewModel.removeFollower(follower)
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "${follower.name} removed",
                                        actionLabel = "Undo"
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.undoRemove()
                                    }
                                }
                            },
                            onFollowToggle = { viewModel.toggleFollowerFollowState(follower.name) }
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = selectedStory != null,
                enter = fadeIn(tween(400)),
                exit = fadeOut(tween(400))
            ) {
                selectedStory?.let { story -> StoryScreen(story = story, onClose = { selectedStory = null }) }
            }

            if (showUnfollowDialog) {
                AlertDialog(
                    onDismissRequest = { showUnfollowDialog = false },
                    title = { Text("Unfollow?") },
                    text = { Text("Are you sure you want to unfollow ${viewModel.name}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.toggleFollowMain()
                            showUnfollowDialog = false
                            scope.launch { snackbarHostState.showSnackbar("You unfollowed ${viewModel.name}") }
                        }) { Text("Unfollow", color = androidx.compose.ui.graphics.Color.Red) }
                    },
                    dismissButton = {
                        TextButton(onClick = { showUnfollowDialog = false }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val nameState = rememberSaveable { mutableStateOf(viewModel.name) }
    val bioState = rememberSaveable { mutableStateOf(viewModel.bio) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Profile",
                        style = TextStyle(
                            fontFamily = Lato,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkPurple
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = LightPurple)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = {
                        nameState.value = it
                        viewModel.updateName(it)
                    },
                    label = { Text("Name", color = DarkPurple) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = Lato, fontSize = 18.sp, color = DarkPurple)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = bioState.value,
                    onValueChange = {
                        bioState.value = it
                        viewModel.updateBio(it)
                    },
                    label = { Text("Bio", color = DarkPurple) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(fontFamily = Lato, fontSize = 16.sp, color = DarkPurple)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = { onBack() }, modifier = Modifier.weight(1f).height(48.dp)) {
                        Text("Back", fontFamily = Lato)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(onClick = {
                        viewModel.updateProfile(nameState.value, bioState.value)
                        scope.launch { snackbarHostState.showSnackbar("Saved") }
                        onBack()
                    }, modifier = Modifier.weight(1f).height(48.dp)) {
                        Text("Save", fontFamily = Lato)
                    }
                }
            }
        }
    }
}

@Composable
fun StoryScreen(story: Story, onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.95f))
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
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 22.sp,
                fontFamily = Lato,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(40.dp))
            TextButton(onClick = onClose) {
                Text("Закрыть", color = androidx.compose.ui.graphics.Color.White, fontSize = 18.sp)
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
            .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(12.dp))
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
                    style = TextStyle(fontFamily = Lato, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = DarkPurple)
                )
            }
            Button(
                onClick = onFollowToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (follower.isFollowing) DarkPurple else GoldYellow
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = if (follower.isFollowing) "Unfollow" else "Follow",
                    color = if (follower.isFollowing) androidx.compose.ui.graphics.Color.White else DarkPurple,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
