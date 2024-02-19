package org.lenuscreations.lelib.bukkit.processor;

import com.google.auto.service.AutoService;
import lombok.SneakyThrows;
import org.lenuscreations.lelib.bukkit.Plugin;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
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

    private ProcessingEnvironment processingEnv;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.processingEnv = processingEnv;
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Plugin.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                Plugin pluginInfo = typeElement.getAnnotation(Plugin.class);

                String mainClassName = typeElement.getQualifiedName().toString();
                if (!processingEnv.getTypeUtils().isAssignable(typeElement.asType(),
                        processingEnv.getElementUtils().getTypeElement("org.bukkit.plugin.java.JavaPlugin").asType())) {
                    if (pluginInfo.generatedClassName().isEmpty()) {
                        messager.printMessage(Diagnostic.Kind.ERROR, "The class annotated with @Plugin cannot have the generatedClassName attribute empty.");
                        return false;
                    }

                    String oldMainClassName = mainClassName;
                    mainClassName = mainClassName + pluginInfo.generatedClassName();

                    this.generateMainClass(typeElement, mainClassName, oldMainClassName);
                }

                // Generate plugin.yml content
                String pluginYmlContent = generatePluginYml(pluginInfo, mainClassName, typeElement);

                // Write content to the plugin.yml file
                writePluginYml(typeElement, pluginYmlContent);

            }
        }
        return true;
    }

    private String generatePluginYml(Plugin pluginInfo, String mainClassName, TypeElement typeElement) {
        return "name: " + pluginInfo.name() + "\n" +
                "version: " + pluginInfo.version() + "\n" +
                "main: " + mainClassName + "\n" +
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

            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    private void generateMainClass(TypeElement typeElement, String mainClassName, String originalMainClassName) {
        String simpleClassName = mainClassName.split("\\.")[mainClassName.split("\\.").length - 1];
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        String template = String.format("package %s;\n\n" +
                "import org.bukkit.plugin.java.JavaPlugin;\n\n" +
                "public class %s extends JavaPlugin {\n" +
                "    \n" +
                "    private org.lenuscreations.lelib.bukkit.Initialiser initialiser;\n" +
                "    private %s clazz;" +
                "    \n" +
                "    @Override\n" +
                "    public void onEnable() {\n" +
                "        initialiser = new org.lenuscreations.lelib.bukkit.Initialiser();\n" +
                "        clazz = new %s();\n" +
                "        initialiser.initialise(this, clazz.getClass());\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    public void onDisable() {\n" +
                "        initialiser.disable(this, clazz.getClass());\n" +
                "    }\n" +
                "}", packageName, simpleClassName, originalMainClassName, originalMainClassName);

        FileObject fileObject = processingEnv.getFiler().createSourceFile(mainClassName, typeElement);
        BufferedWriter writer = new BufferedWriter(fileObject.openWriter());
        writer.write(template);
        writer.close();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
