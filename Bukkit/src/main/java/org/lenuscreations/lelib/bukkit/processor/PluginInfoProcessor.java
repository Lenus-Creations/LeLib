package org.lenuscreations.lelib.bukkit.processor;

import com.google.auto.service.AutoService;
import org.lenuscreations.lelib.bukkit.Plugin;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.lenuscreations.lelib.bukkit.Plugin")
public class PluginInfoProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Plugin.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                Plugin pluginInfo = typeElement.getAnnotation(Plugin.class);

                // Generate plugin.yml content
                String pluginYmlContent = generatePluginYml(pluginInfo, typeElement);

                // Write content to the plugin.yml file
                writePluginYml(typeElement, pluginYmlContent);
            }
        }
        return true;
    }

    private String generatePluginYml(Plugin pluginInfo, TypeElement typeElement) {
        return "name: " + pluginInfo.name() + "\n" +
                "version: " + pluginInfo.version() + "\n" +
                "main: " + typeElement.getQualifiedName() + "\n" +
                (pluginInfo.description().isEmpty() ? "" : "description: " + pluginInfo.description() + "\n") +
                "authors: [" + String.join(",", pluginInfo.authors()) + "]" +
                (pluginInfo.website().isEmpty() ? "" : "\nwebsite: " + pluginInfo.website() + "\n") +
                (pluginInfo.depend().length == 0 ? "" : "depend: [" + String.join(",", pluginInfo.depend()) + "]\n") +
                (pluginInfo.softDepend().length == 0 ? "" : "softdepend: [" + String.join(",", pluginInfo.softDepend()) + "]\n");
    }

    private void writePluginYml(TypeElement typeElement, String content) {
        try {
            FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "plugin.yml", typeElement);
            File file = new File(fileObject.toUri());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            System.out.println("PROCESSORRRR" + file.getAbsolutePath());
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
