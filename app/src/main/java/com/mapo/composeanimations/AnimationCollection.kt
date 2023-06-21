package com.mapo.composeanimations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mapo.composeanimations.R

// @Preview : can't preview animateFloatAsState and animate*AsState
@Composable
private fun FanAnimation() {
    var isRotated by rememberSaveable { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isRotated) 360F else 0f,
        animationSpec = tween(durationMillis = 2500)
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(R.drawable.fan),
            contentDescription = "fan",
            modifier = Modifier
                .rotate(rotationAngle)
                .size(150.dp)
        )

        Button(
            onClick = { isRotated = !isRotated },
            modifier = Modifier
                .padding(top = 50.dp)
                .width(200.dp)
        ) {
            Text(text = "Rotate Fan")
        }
    }
}



@Preview
@Composable
private fun RocketLaunchingAnimation() {
    var isAnimated by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isAnimated, label = "transition")

    val rocketOffset by transition.animateOffset(transitionSpec = {
        if (this.targetState) {
            tween(1000) // launch duration

        } else {
            tween(1500) // land duration
        }
    }, label = "rocket offset") { animated ->
        if (animated) Offset(200f, 0f) else Offset(200f, 500f)
    }

    val rocketSize by transition.animateDp(transitionSpec = {
        tween(1000)
    }, "") { animated ->
        if (animated) 75.dp else 150.dp
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.rocket),
            contentDescription = "Rocket",
            modifier = Modifier
                .size(rocketSize)
                .alpha(1.0f)
                .offset(rocketOffset.x.dp, rocketOffset.y.dp)
        )
        Button(
            onClick = { isAnimated = !isAnimated },
            modifier = Modifier.padding(top = 10.dp)
        ) {
            Text(text = if (isAnimated) "Land rocket" else "Launch rocket")
        }
    }
}

// @Preview : can't preview infiniteTransition.animateFloat
@Composable
private fun InfiniteAnimation() {
    val infiniteTransition = rememberInfiniteTransition()

    val heartSize by infiniteTransition.animateFloat(
        initialValue = 100.0f,
        targetValue = 250.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, delayMillis = 100,easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Image(
        painter = painterResource(R.drawable.heart),

        contentDescription = "heart",
        modifier = Modifier
            .size(heartSize.dp)
    )
}