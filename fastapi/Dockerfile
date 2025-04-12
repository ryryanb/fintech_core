# Use the official Python base image
FROM python:3.9-slim

# Set the working directory
WORKDIR /app

# Set the default port for Cloud Run
ENV PORT=8080

# Copy the requirements.txt file into the container
COPY requirements.txt /app/

# Install the required dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Copy the FastAPI application into the container
COPY . /app/

# Expose the port FastAPI will run on
EXPOSE 5000

# Run the FastAPI application with Uvicorn
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "5000", "--reload"]
