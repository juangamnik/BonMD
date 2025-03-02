import { App, Plugin, PluginSettingTab, Setting, TFile, Menu, Notice, requestUrl } from "obsidian";

// Definiere das Interface für die Plugin-Einstellungen
interface SendMarkdownHttpSettings {
  targetUrl: string; // Ziel-URL für den HTTP-Request
  apiKey?: string;   // Optionaler API-Key für die Authentifizierung
}

// Standardwerte für die Einstellungen
const DEFAULT_SETTINGS: SendMarkdownHttpSettings = {
  targetUrl: "https://example.com/api",
  apiKey: undefined, // Standardmäßig nicht gesetzt
};

export default class SendMarkdownHttp extends Plugin {
  settings: SendMarkdownHttpSettings = {} as SendMarkdownHttpSettings;

  async onload() {
    await this.loadSettings();
    console.log("SendMarkdownHttp Plugin geladen mit Einstellungen:", this.settings);

    // Füge einen Menüeintrag im Datei-Kontextmenü hinzu
    this.registerEvent(
      this.app.workspace.on("file-menu", (menu: Menu, file: TFile) => {
        if (file.extension === "md") {
          menu.addItem((item) => {
            item
              .setTitle("Drucken")
              .setIcon("printer")
              .onClick(() => this.sendFileContent(file));
          });
        }
      })
    );

    // Einstellungs-Tab zur Obsidian-Oberfläche hinzufügen
    this.addSettingTab(new SendMarkdownHttpSettingTab(this.app, this));
  }

  // Funktion zum Senden des Datei-Inhalts
  async sendFileContent(file: TFile) {
    try {
      const content = await this.app.vault.read(file);
      if (!this.settings.targetUrl) {
        new Notice("Keine Ziel-URL konfiguriert.");
        return;
      }

      // Erstelle die Header
      let headers: Record<string, string> = {
        "Content-Type": "text/plain",
      };

      // Falls ein API-Key gesetzt ist, füge ihn als Bearer-Token hinzu
      if (this.settings.apiKey) {
        headers["Authorization"] = `Bearer ${this.settings.apiKey}`;
      }

      // Sende die Anfrage
      const response = await requestUrl({
        url: this.settings.targetUrl,
        method: "POST",
        headers: headers,
        body: content,
      });

      if (response.status === 200) {
        new Notice("Datei erfolgreich gesendet.");
      } else {
        new Notice(`Fehler beim Senden: ${response.status}`);
      }
    } catch (error) {
      console.error("Fehler:", error);
      new Notice("Ein unerwarteter Fehler ist aufgetreten.");
    }
  }

  // Laden der Plugin-Einstellungen
  async loadSettings() {
    this.settings = Object.assign({}, DEFAULT_SETTINGS, await this.loadData());
  }

  // Speichern der Plugin-Einstellungen
  async saveSettings() {
    await this.saveData(this.settings);
  }
}

// Einstellungs-Tab für das Plugin
class SendMarkdownHttpSettingTab extends PluginSettingTab {
  plugin: SendMarkdownHttp;

  constructor(app: App, plugin: SendMarkdownHttp) {
    super(app, plugin);
    this.plugin = plugin;
  }

  display(): void {
    const { containerEl } = this;
    containerEl.empty();

    containerEl.createEl("h2", { text: "SendMarkdownHttp Einstellungen" });

    // Eingabefeld für die Ziel-URL
    new Setting(containerEl)
      .setName("Ziel-URL")
      .setDesc("Die URL, an die die Markdown-Datei gesendet wird.")
      .addText((text) =>
        text
          .setPlaceholder("https://example.com/api")
          .setValue(this.plugin.settings.targetUrl)
          .onChange(async (value) => {
            this.plugin.settings.targetUrl = value;
            await this.plugin.saveSettings();
          })
      );

    // Eingabefeld für den API-Key
    new Setting(containerEl)
      .setName("API-Key (optional)")
      .setDesc("Falls gesetzt, wird der API-Key als Bearer-Token in der Anfrage verwendet.")
      .addText((text) =>
        text
          .setPlaceholder("API-Key eingeben")
          .setValue(this.plugin.settings.apiKey || "")
          .onChange(async (value) => {
            this.plugin.settings.apiKey = value.trim() || undefined;
            await this.plugin.saveSettings();
          })
      );
  }
}