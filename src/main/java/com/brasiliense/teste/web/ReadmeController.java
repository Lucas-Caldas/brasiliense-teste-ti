package com.brasiliense.teste.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ReadmeController {

    @GetMapping(value = "/readme", produces = MediaType.TEXT_HTML_VALUE)
    public String readme() throws IOException {
        String markdown = carregarReadme();
        String escaped = escapeHtml(markdown);
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1" />
                  <title>README - Brasiliense F SIS-03</title>
                  <style>
                    body { margin: 0; background: #f4f6f8; font-family: Segoe UI, Arial, sans-serif; color: #1a1a1a; }
                    header { background: #0b3d91; color: #fff; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; gap: 12px; flex-wrap: wrap; }
                    header a { color: #fff; text-decoration: none; background: #1d4f9c; padding: 8px 12px; border-radius: 6px; font-size: 0.9rem; }
                    main { max-width: 900px; margin: 20px auto; padding: 0 16px 40px; }
                    .card { background: #fff; border: 1px solid #d9dee7; border-radius: 8px; padding: 20px; }
                    pre { white-space: pre-wrap; word-wrap: break-word; margin: 0; font-family: Consolas, monospace; font-size: 0.92rem; line-height: 1.45; }
                  </style>
                </head>
                <body>
                  <header>
                    <strong>README (somente visualização)</strong>
                    <a href="/">Voltar</a>
                  </header>
                  <main>
                    <div class="card">
                      <pre>%s</pre>
                    </div>
                  </main>
                </body>
                </html>
                """.formatted(escaped);
    }

    private String carregarReadme() throws IOException {
        Resource resource = new ClassPathResource("static/README.md");
        if (resource.exists()) {
            return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        }
        Path path = Path.of("README.md");
        if (Files.exists(path)) {
            return Files.readString(path, StandardCharsets.UTF_8);
        }
        return "README.md não encontrado.";
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
