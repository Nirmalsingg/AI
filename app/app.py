from fastapi import FastAPI
from pydantic import BaseModel

app = FastAPI()

class TextRequest(BaseModel):
    text: str

@app.get("/")
def root():
    return {"message": "Server running"}

@app.post("/analyze")
def analyze(req: TextRequest):
    return {
        "ai_probability": 78,
        "human_probability": 22,
        "result": "Likely AI Generated"
    }