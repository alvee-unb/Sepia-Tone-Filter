# Sepia-Tone-Filter

The Sepia Tone Filter converts a JPG image by applying multiple variations of Sepia-tone Filter on different resultant images.
The program operates on each pixel of the image and leverages the SIMD instructions by autovectorization.
The operation for applying Sepia-tone into images is divided into two parts: converting the image into grayscale, and setting the `Sepia-tone depth` for the image.
Autovectorization is triggered for the former operation.
The elapsed average execution time for target method is displayed after the operation.

## Usage

```
cd src
javac *.java
java SepiaToneFilter <JPG_IMAGE_PATH> <ITERATIONS>
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE "LICENSE") file for details.