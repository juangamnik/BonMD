# BonMD - Backend

BonMD - Backend is a Spring Boot microservice written in Kotlin. It converts Markdown (MD) files into PDF documents by rendering Markdown to HTML and then to PDF using pure JVM libraries. LLMs have been used to generate code and documentation for this project.

## Features

- **Markdown to PDF Conversion:** Transforms MD files into scalable PDF documents.
- **Thermal Printing Integration:** Sends PDF output directly to a thermal printer (80mm infinite paper). The printer is configured via the mandatory `PRINTER` environment variable.
- **Spring Boot Microservice:** Built with Kotlin on Spring Boot.
- **Markdown Rendering:** Utilizes JVM libraries for converting Markdown → HTML → PDF.
- **API-Tokens:** The `API_TOKENS` environment variable holds a comma-separated list of valid API tokens as a means of a simple authorization mechanism

## Getting Started

### Prerequisites

- **Java Runtime:** Ensure a Java version is installed (I tested the service on openjdk v23).
- **Gradle:** Use the provided Gradle wrapper (`gradlew` or `gradlew.bat`).
- **Printer Setup:** A thermal printer (80mm infinite paper) shouold be connected. The `PRINTER` environment variable is mandatory.
- **Operating System:** Compatible with UNIX, macOS, Windows.

### Installation

1. **Clone the Repository:**

    ```bash
    git clone https://github.com/juangamnik/BonMD.git
    cd BonMD/backend
    ```

2. **Build the Project:**

    On Unix-based systems:
    ```bash
    ./gradlew build
    ```
    On Windows:
    ```bash
    gradlew.bat build
    ```

### Configuration

Before starting the Spring Boot application, set the following environment variables:

```bash
export PRINTER="MyThermalPrinter"
export API_TOKENS="123e4567-e89b-12d3-a456-426614174000-example,987e6543-e21b-12d3-a456-426614174111-example"
```

These variables configure:
- **PRINTER:** The printer name or identifier (mandatory).
- **API_TOKENS:** A comma-separated list of valid API tokens.

## Usage

Start the backend with:

```bash
./gradlew bootRun
```
or on Windows:
```bash
gradlew.bat bootRun
```

The service listens for incoming requests on port 8080 and processes Markdown files accordingly.

### Example: Calling the Backend with cURL

The backend exposes a POST endpoint at `/api/printMarkdown`. You can call it directly from your terminal. The `API_TOKENS` environment variable contains all valid tokens, the API call expects one of these tokens in the `Authorization` header. You can inject the content of a Markdown file using command substitution. For example:

```
curl -X POST "http://127.0.0.1:8080/api/printMarkdown" \
     -H "Content-Type: text/plain" \
     -H "Authorization: Bearer 123e4567-e89b-12d3-a456-426614174000-example" \
     --data-binary "$(cat 'example.md')"
```

In this example:
- The **Authorization** header includes one API token from the configured list.
- The command substitution `$(cat 'example.md')` injects the content of `example.md` into the request body.
- The **Content-Type** header is set to `text/plain` since the payload is plain text.

## Roadmap / Issues

- Markdown:
	- Strike through text
	- Right table border currently invisible
	- Adapt table column widths
	- Better presentation of TODO-Lists
- Other formats than 80mm endless paper for thermal printers like DIN A4

## Contributing

Contributions are welcome! To contribute:

1. **Fork the Repository**
2. **Create a Feature Branch**
3. **Commit Your Changes** (follow standard coding practices and add tests)
4. **Open a Pull Request**

For major changes, please open an issue to discuss your ideas first.

## License

This project is released under the [MIT License](../LICENSE). The Meslo font from Nerd Fonts used in this project is also licensed under MIT. See the Nerd Font LICENSE in the root folder for details.

## Contact

If you have any questions or need support, please open an issue on GitHub.
