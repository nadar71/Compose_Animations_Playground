package com.mapo.composeanimations.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.mapo.composeanimations.R


// from :
// https://developer.android.com/jetpack/compose/animation/composables-modifiers
// https://github.com/android/snippets/blob/e9e6e1fc71b9a6fb77277126ad44e985deea992d/compose/snippets/src/main/java/com/example/compose/snippets/animations/AnimationSnippets.kt#L313-L346
@Preview
@Composable
private fun AnimatedVisibilitySample() {
    // [START android_compose_animations_animated_visibility]
    var editable by remember { mutableStateOf(true) }
    AnimatedVisibility(visible = editable) {
        Text(text = "Edit")
    }
    // [END android_compose_animations_animated_visibility]
}


@Preview
@Composable
private fun AnimatedVisibilityWithEnterAndExit() {
    // [START android_compose_animations_animated_visibility_enter_exit]
    var visible by remember { mutableStateOf(true) }
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically {
            // Slide in from 40 dp from the top.
            with(density) { -200.dp.roundToPx() }
        } + expandVertically(
            // Expand from the top.
            expandFrom = Alignment.Top
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Text("Hello", Modifier
            .fillMaxWidth()
            .height(100.dp))
    }
    // [END android_compose_animations_animated_visibility_enter_exit]
}


@Preview
@Composable
private fun AnimatedVisibilityWithEnterAndExit_01() {
    // [START android_compose_animations_animated_visibility_enter_exit]
    var visible by remember { mutableStateOf(true) }
    val density = LocalDensity.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically (
            // animation elapse time 1 sec
            // different FiniteAnimationSpec:
            // TweenSpec, SpringSpec, KeyframesSpec, RepeatableSpec, SnapSpec, etc.
            // By definition, InfiniteRepeatableSpec does not implement this interface.
            animationSpec = tween(durationMillis = 100),
        ) {
            // param initialOffsetY : Slide in from 40 dp from the top.
            // another way : initialOffset = { density -> -40.dp.roundToPx() }
            with(density) { -40.dp.roundToPx() }
            // another value :  initialOffset = { fullHeight -> -fullHeight / 2 }
        } + expandVertically(
            // Expand from the bottom.
            expandFrom = Alignment.Bottom
        ) + fadeIn(
            // Fade in with the initial alpha of 0.3f.
            initialAlpha = 0.3f
        ) /*+ scaleIn(
            animationSpec = spring(dampingRatio = 0.0f, stiffness = 0.8f),
            initialScale = 0.3f
        ),*/,
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Text("Hello", Modifier
            .fillMaxWidth()
            .height(100.dp))
    }
    // [END android_compose_animations_animated_visibility_enter_exit]
}


@Preview
@Composable
private fun AnimatedVisibilityMutable() {
    // [START android_compose_animations_animated_visibility_mutable]
    // Create a MutableTransitionState<Boolean> for the AnimatedVisibility.
    // detect if the animation is running or finished running
    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    Column {
        AnimatedVisibility(visibleState = state) {
            Text(text = "Hello, world!")
        }

        // Use the MutableTransitionState to know the current animation state
        // of the AnimatedVisibility.
        // isIdle is when current animation has finished running
        Text(
            text = when {
                state.isIdle && state.currentState -> "Visible"
                !state.isIdle && state.currentState -> "Disappearing"
                state.isIdle && !state.currentState -> "Invisible"
                else -> "Appearing"
            }
        )
    }
    // [END android_compose_animations_animated_visibility_mutable]
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
@Preview
private fun AnimatedVisibilityAnimateEnterExitChildren() {
    // [START android_compose_animations_animated_visibility_animate_enter_exit_children]
    var visible by remember { mutableStateOf(true) }
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        // Fade in/out the background and the foreground.
        Box(Modifier.fillMaxSize().background(Color.DarkGray)) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .animateEnterExit(
                        // Slide in/out the inner box.
                        enter = slideInVertically(
                            animationSpec = tween(500,
                                0,
                                easing = FastOutLinearInEasing)
                        ),
                        exit = slideOutVertically(
                            animationSpec = tween(500,
                                0,
                                easing = FastOutLinearInEasing)
                        )
                    )
                    .sizeIn(minWidth = 256.dp, minHeight = 64.dp)
                    .background(Color.Red)
            ) {
                // Content of the notification…
            }
        }
    }
    // [END android_compose_animations_animated_visibility_animate_enter_exit_children]
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
private fun AnimatedVisibilityTransition() {
    // [START android_compose_animations_animated_visibility_transition]
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) { // this: AnimatedVisibilityScope
        // Use AnimatedVisibilityScope#transition to add a custom animation
        // to the AnimatedVisibility.
        val background by transition.animateColor(label = "color") { state ->
            if (state == EnterExitState.Visible) Color.Blue else Color.Gray
        }
        // NB: to be verified : the code with by could be written using this expression as well :
        /*
            val transition = rememberInfiniteTransition()
            val background = transition.animateColor(
                label = "color",
                initialValue = Color.Gray,
                targetValue = Color.Blue,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000),
                    repeatMode = RepeatMode.Reverse
                )
            )
        */
        Box(modifier = Modifier.size(128.dp).background(background))
    }
    // [END android_compose_animations_animated_visibility_transition]
}

// START FROM HERE

@Composable
@Preview
private fun AnimateAsStateSimple() {
    // [START android_compose_animations_animate_as_state]
    var enabled by remember { mutableStateOf(true) }

    val alpha: Float by animateFloatAsState(if (enabled) 1f else 0.5f)
    Box(
        Modifier.fillMaxSize()
            .graphicsLayer(alpha = alpha)
            .background(Color.Red)
    )
    // [END android_compose_animations_animate_as_state]
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
private fun AnimatedContentSimple() {
    // [START android_compose_animations_animated_content_simple]
    Row {
        var count by remember { mutableStateOf(0) }
        Button(onClick = { count++ }) {
            Text("Add")
        }
        AnimatedContent(targetState = count) { targetCount ->
            // Make sure to use `targetCount`, not `count`.
            Text(text = "Count: $targetCount")
        }
    }
    // [END android_compose_animations_animated_content_simple]
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedContentTransitionSpec(count: Int) {
    // [START android_compose_animations_animated_content_transition_spec]
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            // Compare the incoming number with the previous number.
            if (targetState > initialState) {
                // If the target number is larger, it slides up and fades in
                // while the initial (smaller) number slides up and fades out.
                slideInVertically { height -> height } + fadeIn() with
                        slideOutVertically { height -> -height } + fadeOut()
            } else {
                // If the target number is smaller, it slides down and fades in
                // while the initial number slides down and fades out.
                slideInVertically { height -> -height } + fadeIn() with
                        slideOutVertically { height -> height } + fadeOut()
            }.using(
                // Disable clipping since the faded slide-in/out should
                // be displayed out of bounds.
                SizeTransform(clip = false)
            )
        }
    ) { targetCount ->
        Text(text = "$targetCount")
    }
    // [END android_compose_animations_animated_content_transition_spec]
}


/*@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun AnimatedContentSizeTransform() {
    // [START android_compose_animations_animated_content_size_transform]
    var expanded by remember { mutableStateOf(false) }
    Surface(
        color = MaterialTheme.colorScheme.primary,
        onClick = { expanded = !expanded }
    ) {
        AnimatedContent(
            targetState = expanded,
            transitionSpec = {
                fadeIn(animationSpec = tween(150, 150)) with
                        fadeOut(animationSpec = tween(150)) using
                        SizeTransform { initialSize, targetSize ->
                            if (targetState) {
                                keyframes {
                                    // Expand horizontally first.
                                    IntSize(targetSize.width, initialSize.height) at 150
                                    durationMillis = 300
                                }
                            } else {
                                keyframes {
                                    // Shrink vertically first.
                                    IntSize(initialSize.width, targetSize.height) at 150
                                    durationMillis = 300
                                }
                            }
                        }
            }
        ) { targetExpanded ->
            if (targetExpanded) {
                Expanded()
            } else {
                ContentIcon()
            }
        }
    }
    // [END android_compose_animations_animated_content_size_transform]
}*/

@Composable
private fun AnimateContentSizeSimple() {
    // [START android_compose_animations_animated_content_size_modifier_simple]
    var message by remember { mutableStateOf("Hello") }
    Box(
        modifier = Modifier.background(Color.Blue).animateContentSize()
    ) { Text(text = message) }
    // [END android_compose_animations_animated_content_size_modifier_simple]
}

@Composable
private fun CrossfadeSimple() {
    // [START android_compose_animations_crossfade_simple]
    var currentPage by remember { mutableStateOf("A") }
    Crossfade(targetState = currentPage) { screen ->
        when (screen) {
            "A" -> Text("Page A")
            "B" -> Text("Page B")
        }
    }
    // [END android_compose_animations_crossfade_simple]
}

private object UpdateTransitionEnumState {
    // [START android_compose_animations_transitions_box_state]
    enum class BoxState {
        Collapsed,
        Expanded
    }
    // [END android_compose_animations_transitions_box_state]

    @Composable
    private fun UpdateTransitionInstance() {
        // [START android_compose_animations_transitions_instance]
        var currentState by remember { mutableStateOf(BoxState.Collapsed) }
        val transition = updateTransition(currentState, label = "box state")
        // [END android_compose_animations_transitions_instance]
    }

    @Composable
    private fun UpdateTransitionAnimationValues(transition: Transition<BoxState>) {
        // [START android_compose_animations_transitions_values]
        val rect by transition.animateRect(label = "rectangle") { state ->
            when (state) {
                BoxState.Collapsed -> Rect(0f, 0f, 100f, 100f)
                BoxState.Expanded -> Rect(100f, 100f, 300f, 300f)
            }
        }
        val borderWidth by transition.animateDp(label = "border width") { state ->
            when (state) {
                BoxState.Collapsed -> 1.dp
                BoxState.Expanded -> 0.dp
            }
        }
        // [END android_compose_animations_transitions_values]
    }

    @Composable
    private fun UpdateTransitionTransitionSpec(transition: Transition<BoxState>) {
        // [START android_compose_animations_transitions_spec]
        val color by transition.animateColor(
            transitionSpec = {
                when {
                    BoxState.Expanded isTransitioningTo BoxState.Collapsed ->
                        spring(stiffness = 50f)
                    else ->
                        tween(durationMillis = 500)
                }
            }, label = "color"
        ) { state ->
            when (state) {
                BoxState.Collapsed -> MaterialTheme.colorScheme.primary
                BoxState.Expanded -> MaterialTheme.colorScheme.background
            }
        }
        // [END android_compose_animations_transitions_spec]
    }

    @Composable
    private fun UpdateTransitionMutableTransitionState() {
        // [START android_compose_animations_transitions_state]
        // Start in collapsed state and immediately animate to expanded
        var currentState = remember { MutableTransitionState(BoxState.Collapsed) }
        currentState.targetState = BoxState.Expanded
        val transition = updateTransition(currentState, label = "box state")
        // ……
        // [END android_compose_animations_transitions_state]
    }
}

@OptIn(ExperimentalTransitionApi::class)
private object UpdateTransitionCreateChildTransition {

    // [START android_compose_animations_transitions_dialer_example]
    enum class DialerState { DialerMinimized, NumberPad }

    @Composable
    fun DialerButton(isVisibleTransition: Transition<Boolean>) {
        // `isVisibleTransition` spares the need for the content to know
        // about other DialerStates. Instead, the content can focus on
        // animating the state change between visible and not visible.
    }

    @Composable
    fun NumberPad(isVisibleTransition: Transition<Boolean>) {
        // `isVisibleTransition` spares the need for the content to know
        // about other DialerStates. Instead, the content can focus on
        // animating the state change between visible and not visible.
    }

    @Composable
    fun Dialer(dialerState: DialerState) {
        val transition = updateTransition(dialerState, label = "dialer state")
        Box {
            // Creates separate child transitions of Boolean type for NumberPad
            // and DialerButton for any content animation between visible and
            // not visible
            NumberPad(
                transition.createChildTransition {
                    it == DialerState.NumberPad
                }
            )
            DialerButton(
                transition.createChildTransition {
                    it == DialerState.DialerMinimized
                }
            )
        }
    }
    // [END android_compose_animations_transitions_dialer_example]
}

/*@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Composable
private fun UpdateTransitionAnimatedVisibility() {
    // [START android_compose_animations_transitions_animated_visibility]
    var selected by remember { mutableStateOf(false) }
    // Animates changes when `selected` is changed.
    val transition = updateTransition(selected, label = "selected state")
    val borderColor by transition.animateColor(label = "border color") { isSelected ->
        if (isSelected) Color.Magenta else Color.White
    }
    val elevation by transition.animateDp(label = "elevation") { isSelected ->
        if (isSelected) 10.dp else 2.dp
    }
    Surface(
        onClick = { selected = !selected },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, borderColor),
        elevation = elevation
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(text = "Hello, world!")
            // AnimatedVisibility as a part of the transition.
            transition.AnimatedVisibility(
                visible = { targetSelected -> targetSelected },
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Text(text = "It is fine today.")
            }
            // AnimatedContent as a part of the transition.
            transition.AnimatedContent { targetState ->
                if (targetState) {
                    Text(text = "Selected")
                } else {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = "Phone")
                }
            }
        }
    }
    // [END android_compose_animations_transitions_animated_visibility]
}*/

private object UpdateTransitionEncapsulating {
    // [START android_compose_animations_transitions_encapsulating]
    enum class BoxState { Collapsed, Expanded }

    @Composable
    fun AnimatingBox(boxState: BoxState) {
        val transitionData = updateTransitionData(boxState)
        // UI tree
        Box(
            modifier = Modifier
                .background(transitionData.color)
                .size(transitionData.size)
        )
    }

    // Holds the animation values.
    private class TransitionData(
        color: State<Color>,
        size: State<Dp>
    ) {
        val color by color
        val size by size
    }

    // Create a Transition and return its animation values.
    @Composable
    private fun updateTransitionData(boxState: BoxState): TransitionData {
        val transition = updateTransition(boxState, label = "box state")
        val color = transition.animateColor(label = "color") { state ->
            when (state) {
                BoxState.Collapsed -> Color.Gray
                BoxState.Expanded -> Color.Red
            }
        }
        val size = transition.animateDp(label = "size") { state ->
            when (state) {
                BoxState.Collapsed -> 64.dp
                BoxState.Expanded -> 128.dp
            }
        }
        return remember(transition) { TransitionData(color, size) }
    }
    // [END android_compose_animations_transitions_encapsulating]
}

@Composable
private fun RememberInfiniteTransitionSimple() {
    // [START android_compose_animations_infinite_transition_simple]
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(Modifier.fillMaxSize().background(color))
    // [END android_compose_animations_infinite_transition_simple]
}

@Composable
private fun AnimatableSimple(ok: Boolean) {
    // [START android_compose_animations_animatable_simple]
    // Start out gray and animate to green/red based on `ok`
    val color = remember { Animatable(Color.Gray) }
    LaunchedEffect(ok) {
        color.animateTo(if (ok) Color.Green else Color.Red)
    }
    Box(Modifier.fillMaxSize().background(color.value))
    // [END android_compose_animations_animatable_simple]
}
@Composable
private fun TargetBasedAnimationSimple(someCustomCondition: () -> Boolean) {
    // [START android_compose_animations_target_based_animation_simple]
    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(200),
            typeConverter = Float.VectorConverter,
            initialValue = 200f,
            targetValue = 1000f
        )
    }
    var playTime by remember { mutableStateOf(0L) }

    LaunchedEffect(anim) {
        val startTime = withFrameNanos { it }

        do {
            playTime = withFrameNanos { it } - startTime
            val animationValue = anim.getValueFromNanos(playTime)
        } while (someCustomCondition())
    }
    // [END android_compose_animations_target_based_animation_simple]
}

@Composable
private fun AnimationSpecTween(enabled: Boolean) {
    // [START android_compose_animations_spec_tween]
    val alpha: Float by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        // Configure the animation duration and easing.
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
    // [END android_compose_animations_spec_tween]
}

@Composable
private fun AnimationSpecSpring() {
    // [START android_compose_animations_spec_spring]
    val value by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )
    // [END android_compose_animations_spec_spring]
}

@Composable
private fun AnimationSpecTween() {
    // [START android_compose_animations_spec_tween_delay]
    val value by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 50,
            easing = LinearOutSlowInEasing
        )
    )
    // [END android_compose_animations_spec_tween_delay]
}

@Composable
private fun AnimationSpecKeyframe() {
    // [START android_compose_animations_spec_keyframe]
    val value by animateFloatAsState(
        targetValue = 1f,
        animationSpec = keyframes {
            durationMillis = 375
            0.0f at 0 with LinearOutSlowInEasing // for 0-15 ms
            0.2f at 15 with FastOutLinearInEasing // for 15-75 ms
            0.4f at 75 // ms
            0.4f at 225 // ms
        }
    )
    // [END android_compose_animations_spec_keyframe]
}

@Composable
private fun AnimationSpecRepeatable() {
    // [START android_compose_animations_spec_repeatable]
    val value by animateFloatAsState(
        targetValue = 1f,
        animationSpec = repeatable(
            iterations = 3,
            animation = tween(durationMillis = 300),
            repeatMode = RepeatMode.Reverse
        )
    )
    // [END android_compose_animations_spec_repeatable]
}

@Composable
private fun AnimationSpecInfiniteRepeatable() {
    // [START android_compose_animations_spec_infinite_repeatable]
    val value by animateFloatAsState(
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 300),
            repeatMode = RepeatMode.Reverse
        )
    )
    // [END android_compose_animations_spec_infinite_repeatable]
}

@Composable
private fun AnimationSpecSnap() {
    // [START android_compose_animations_spec_snap]
    val value by animateFloatAsState(
        targetValue = 1f,
        animationSpec = snap(delayMillis = 50)
    )
    // [END android_compose_animations_spec_snap]
}

private object Easing {
    // [START android_compose_animations_easing_usage]
    val CustomEasing = Easing { fraction -> fraction * fraction }

    @Composable
    fun EasingUsage() {
        val value by animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 300,
                easing = CustomEasing
            )
        )
        // ……
    }
    // [END android_compose_animations_easing_usage]
}

private object AnimationVectorTwoWayConverter {
    // [START android_compose_animations_vector_convertor]
    val IntToVector: TwoWayConverter<Int, AnimationVector1D> =
        TwoWayConverter({ AnimationVector1D(it.toFloat()) }, { it.value.toInt() })
    // [END android_compose_animations_vector_convertor]
}

private object AnimationVectorCustomType {
    // [START android_compose_animations_vector_convertor_custom_type]
    data class MySize(val width: Dp, val height: Dp)

    @Composable
    fun MyAnimation(targetSize: MySize) {
        val animSize: MySize by animateValueAsState(
            targetSize,
            TwoWayConverter(
                convertToVector = { size: MySize ->
                    // Extract a float value from each of the `Dp` fields.
                    AnimationVector2D(size.width.value, size.height.value)
                },
                convertFromVector = { vector: AnimationVector2D ->
                    MySize(vector.v1.dp, vector.v2.dp)
                }
            )
        )
    }
    // [END android_compose_animations_vector_convertor_custom_type]
}

/*@OptIn(ExperimentalAnimationGraphicsApi::class)
@Preview
// [START android_compose_animations_vector_drawable]
@Composable
fun AnimatedVectorDrawable() {
    val image = AnimatedImageVector.animatedVectorResource(R.drawable.ic_hourglass_animated)
    var atEnd by remember { mutableStateOf(false) }
    Image(
        painter = rememberAnimatedVectorPainter(image, atEnd),
        contentDescription = "Timer",
        modifier = Modifier.clickable {
            atEnd = !atEnd
        },
        contentScale = ContentScale.Crop
    )
}
// [END android_compose_animations_vector_drawable]*/

@Composable
private fun Expanded() {
}

@Composable
private fun ContentIcon() {
}