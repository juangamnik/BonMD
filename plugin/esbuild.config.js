require("esbuild").build({
  entryPoints: ["build/main.js"],
  bundle: true,
  minify: true,
  outfile: "dist/main.js",
  external: ["obsidian"],
  format: "cjs",  // Stellt sicher, dass CommonJS verwendet wird
  target: ["node12"], // Erhält Klassenstrukturen
}).catch(() => process.exit(1));
