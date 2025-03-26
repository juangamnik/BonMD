# SendMarkdownHttp Plugin

## Features
- **Send Markdown to an HTTP Endpoint**: Sends the content of a Markdown file via HTTP POST to a configurable URL.
- **Optional API Key**: Appends a bearer token header if provided.
- **Configurable Menu Item**: Customize both the title and icon of the file context menu entry.

## Installation

Since this plugin is **not** available in the Obsidian Community Plugin directory, you have to install it manually:

1. **Obtain the plugin**  
   - Clone or download the [GitHub repository](https://github.com/juangamnik/BonMD).
   - Go to the `plugin/` folder.

2. **Build (if necessary)**  
   Use the following commands in the plugin directory:
   ```bash
   npm install
   npm run build
   ```

3. **Copy to your vault**  
   - In your Obsidian vault (e.g., `~/Obsidian/MyVault/`), create a new folder under `.obsidian/plugins/`, for example `send-markdown-http`.
   - Copy the compiled files from `dist/` (`main.js`, `manifest.json`, optionally `styles.css`) into that folder.

4. **Enable the plugin**  
   - Open Obsidian and go to **Settings** > **Community Plugins** (enable community plugins if not already done).
   - Find “SendMarkdownHttp” in the list of plugins and toggle it on.

## Usage
1. **Right-click a Markdown file** in your vault.
2. Select the newly added menu item (default: “Drucken”).
3. The plugin sends the file content as plain text to your specified endpoint.

## Configuration
Open **Settings** > **SendMarkdownHttp** and adjust:

- **Target URL**: The endpoint that receives your Markdown.
- **API Key (optional)**: Adds `Authorization: Bearer <apiKey>` to the request header if provided.
- **Menu Item Title**: The text for the context menu entry.
- **Menu Item Icon**: The icon name used for the context menu entry (e.g., “printer”).

## License
This project is licensed under an open-source license. See [LICENSE](./LICENSE) for details.

## Contributing
- Please submit bug reports or feature requests via [GitHub Issues](https://github.com/juangamnik/BonMD/issues).
- Pull requests are welcome!