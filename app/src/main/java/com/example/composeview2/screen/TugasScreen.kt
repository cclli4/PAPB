package com.example.composeview2.screen

import android.Manifest
import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.content.ContextCompat
import com.example.composeview2.local.Tugas
import com.example.composeview2.viewmodel.TugasViewModel
import com.example.composeview2.viewmodel.TugasViewModelFactory
import java.io.File

@Composable
fun CameraPreview(
    showCamera: Boolean,
    onImageCaptured: (Bitmap) -> Unit, // Pass captured image back to parent
    onCameraClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Permission for camera
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
            onCameraClose()
        }
    }

    LaunchedEffect(showCamera) {
        if (showCamera) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var imageCapture: ImageCapture? = remember { null }

    if (showCamera) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().apply {
                                setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                            imageCapture = ImageCapture.Builder()
                                .setTargetRotation(previewView.display.rotation)
                                .build()

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            Log.e("CameraPreview", "Error setting up camera: ${e.message}", e)
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = {
                        // Capture and save the image
                        val photoFile = File(
                            context.externalMediaDirs.firstOrNull(),
                            "photo_${System.currentTimeMillis()}.jpg"
                        )
                        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                        imageCapture?.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    Toast.makeText(context, "Photo saved: ${photoFile.absolutePath}", Toast.LENGTH_SHORT).show()
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Log.e("CameraPreview", "Photo capture failed: ${exception.message}", exception)
                                }
                            }
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Take Picture")
                }

                Button(
                    onClick = onCameraClose,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Close Camera")
                }
            }
        }
    }
}


@Composable
fun TugasScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val tugasViewModel: TugasViewModel = viewModel(factory = TugasViewModelFactory(application))

    var matkulInput by remember { mutableStateOf("") }
    var detailTugasInput by remember { mutableStateOf("") }
    val tugasList by tugasViewModel.allTugas.observeAsState(emptyList())

    var showCamera by remember { mutableStateOf(false) }
    var capturedImage: Bitmap? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Tugas",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = matkulInput,
            onValueChange = { matkulInput = it },
            label = { Text("Nama Matkul") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Book,
                    contentDescription = "Course Icon"
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = detailTugasInput,
            onValueChange = { detailTugasInput = it },
            label = { Text("Detail Tugas") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = "Task Detail Icon"
                )
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        // Preview of the captured image
        capturedImage?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { showCamera = true },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Camera")
            }

            Button(
                onClick = {
                    if (matkulInput.isNotBlank() && detailTugasInput.isNotBlank()) {
                        tugasViewModel.addTugas(Tugas(namaMatkul = matkulInput, detailTugas = detailTugasInput))
                        matkulInput = ""
                        detailTugasInput = ""
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Show camera preview if showCamera is true
        CameraPreview(
            showCamera = showCamera,
            onImageCaptured = { bitmap -> capturedImage = bitmap }, // Set captured image to state
            onCameraClose = { showCamera = false }
        )

        if (tugasList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(tugasList) { tugas ->
                    TugasItem(
                        tugas = tugas,
                        onComplete = {
                            tugasViewModel.toggleCompletion(tugas)
                        }
                    )
                }
            }
        } else {
            Text("No tasks available.", modifier = Modifier.padding(16.dp))
        }
    }
}


@Composable
fun TugasItem(tugas: Tugas, onComplete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (tugas.completed) Color(0xFFE0FFE0) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Nama Matkul: ${tugas.namaMatkul}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (tugas.completed) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Detail Tugas: ${tugas.detailTugas}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (tugas.completed) Color.Gray else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onComplete) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Mark as Complete",
                    tint = if (tugas.completed) Color.Gray else Color.Green
                )
            }
        }
    }
}
