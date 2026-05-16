from fastapi import FastAPI, File, UploadFile, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List
import uvicorn
import os
from detectors import TextDetector, VideoDetector, AppDetector, OtherDetector

app = FastAPI(title="AI Content Detector API")

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Initialize detectors
text_detector = TextDetector()
video_detector = VideoDetector()
app_detector = AppDetector()
other_detector = OtherDetector()


class TextRequest(BaseModel):
    text: str


class Prediction(BaseModel):
    label: str
    score: float


class ApiResponse(BaseModel):
    result: List[Prediction]


@app.post("/detect")
async def detect_text(request: TextRequest):
    """Detect if text is AI-generated or human-written"""
    try:
        result = text_detector.detect(request.text)
        return ApiResponse(result=[Prediction(label=result["label"], score=result["score"])])
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/detect/video")
async def detect_video(video: UploadFile = File(...)):
    """Detect if video is AI-generated or human-created"""
    try:
        # Save uploaded file temporarily
        temp_path = f"temp_{video.filename}"
        with open(temp_path, "wb") as buffer:
            content = await video.read()
            buffer.write(content)
        
        result = video_detector.detect(temp_path)
        
        # Clean up
        if os.path.exists(temp_path):
            os.remove(temp_path)
        
        return ApiResponse(result=[Prediction(label=result["label"], score=result["score"])])
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/detect/app")
async def detect_app(app: UploadFile = File(...)):
    """Detect if application is AI-generated or human-created"""
    try:
        # Save uploaded file temporarily
        temp_path = f"temp_{app.filename}"
        with open(temp_path, "wb") as buffer:
            content = await app.read()
            buffer.write(content)
        
        result = app_detector.detect(temp_path)
        
        # Clean up
        if os.path.exists(temp_path):
            os.remove(temp_path)
        
        return ApiResponse(result=[Prediction(label=result["label"], score=result["score"])])
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/detect/other")
async def detect_other(file: UploadFile = File(...)):
    """Detect if other content (images, audio, etc.) is AI-generated or human-created"""
    try:
        # Save uploaded file temporarily
        temp_path = f"temp_{file.filename}"
        with open(temp_path, "wb") as buffer:
            content = await file.read()
            buffer.write(content)
        
        result = other_detector.detect(temp_path, file.content_type)
        
        # Clean up
        if os.path.exists(temp_path):
            os.remove(temp_path)
        
        return ApiResponse(result=[Prediction(label=result["label"], score=result["score"])])
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/")
async def root():
    return {"message": "AI Content Detector API is running"}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8001)
