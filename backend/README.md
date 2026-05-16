# AI Content Detector Backend API

This is the backend API server for the AI Content Detector Android app. It provides endpoints to detect whether content (text, video, applications, or other files) is AI-generated or human-created.

## Features

- **Text Detection**: Analyzes text to determine if it's AI-generated or human-written
- **Video Detection**: Analyzes video files for AI-generated content
- **Application Detection**: Analyzes APK files for AI-generated applications
- **Other Content Detection**: Supports images, audio, PDFs, and other file types

## Setup Instructions

### Prerequisites

- Python 3.8 or higher
- pip (Python package manager)

### Installation

1. Navigate to the backend directory:
```bash
cd backend
```

2. Install the required dependencies:
```bash
pip install -r requirements.txt
```

## Running the Server

Start the FastAPI server:

```bash
python main.py
```

The server will start on `http://0.0.0.0:8001`

## API Endpoints

### POST /detect
Detect if text is AI-generated or human-written.

**Request Body:**
```json
{
  "text": "Your text here"
}
```

**Response:**
```json
{
  "result": [
    {
      "label": "AI Generated" or "Human Written",
      "score": 0.85
    }
  ]
}
```

### POST /detect/video
Detect if video is AI-generated or human-created.

**Request:** Multipart form data with video file

**Response:**
```json
{
  "result": [
    {
      "label": "AI Generated" or "Human Created",
      "score": 0.75
    }
  ]
}
```

### POST /detect/app
Detect if application is AI-generated or human-created.

**Request:** Multipart form data with APK file

**Response:**
```json
{
  "result": [
    {
      "label": "AI Generated" or "Human Created",
      "score": 0.65
    }
  ]
}
```

### POST /detect/other
Detect if other content (images, audio, PDFs, etc.) is AI-generated or human-created.

**Request:** Multipart form data with file

**Response:**
```json
{
  "result": [
    {
      "label": "AI Generated" or "Human Created",
      "score": 0.70
    }
  ]
}
```

## Note

This implementation uses heuristic-based detection and random scores for demonstration purposes. For production use, you should integrate actual ML models:

- **Text**: Use models like GPTZero, OpenAI's classifier, or train your own
- **Video**: Use deepfake detection models and computer vision algorithms
- **Images**: Use GAN detectors or services like Hive AI, Sensity
- **Audio**: Use audio deepfake detection models
- **Applications**: Analyze code structure, metadata, and patterns

## CORS

The API has CORS enabled to allow requests from the Android app. In production, you should restrict the allowed origins to your specific app domain.

## Development

To modify the detection logic, edit the `detectors.py` file. Each detector class has a `detect` method that returns a dictionary with `label` and `score` keys.
