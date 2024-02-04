package org.lenuscreations.lelib.bukkit.processor;

import com.google.auto.service.AutoService;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;
import org.lenuscreations.lelib.bukkit.Initialiser;
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
import java.lang.reflect.Modifier;
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

                    mainClassName = mainClassName + pluginInfo.generatedClassName();

                    this.generateMainClass(typeElement, mainClassName);
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
    private void generateMainClass(TypeElement typeElement, String mainClassName) {
        /*MethodSpec methodSpec = MethodSpec.methodBuilder("onEnable")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("new " + Initialiser.class.getName() + "().initialise(this, " + typeElement.getQualifiedName() + ".class)")
                .build();

        MethodSpec onDisableMethod = MethodSpec.methodBuilder("onDisable")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("new " + Initialiser.class.getName() + "().disable(this, " + typeElement.getQualifiedName() + ".class)")
                .build();

        TypeSpec.Builder mainClass = TypeSpec.classBuilder(mainClassName.split("\\.")[mainClassName.split("\\.").length - 1])
                .addModifiers(Modifier.PUBLIC)
                .superclass(JavaPlugin.class)
                .addMethod(methodSpec)
                .addMethod(onDisableMethod);

        JavaFile javaFile = JavaFile.builder(mainClassName, mainClass.build())
                .build();*/
        // bytebuddy
        /*ByteBuddyAgent.install();
        ByteBuddy byteBuddy = new ByteBuddy();
        byteBuddy.subclass(JavaPlugin.class)
                .name(mainClassName)
                .defineMethod("onEnable", void.class, Modifier.PUBLIC)
                .intercept(SuperMethodCall.INSTANCE
                        .andThen(
                                MethodCall.invoke(named("initialise"))
                                        .on(new Initialiser())
                                        .withArgument(0, 0)
                                        .withArgument(1, 1)
                        )
                )
                .defineMethod("onDisable", void.class, Modifier.PUBLIC)
                .intercept(SuperMethodCall.INSTANCE
                        .andThen(
                                MethodCall.invoke(named("disable"))
                                        .on(new Initialiser())
                                        .withArgument(0, 0)
                                        .withArgument(1, 1)
                        )
                )
                .make()
                .load(typeElement.getClass().getClassLoader());*/
        // javassist
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(JavaPlugin.class));

        CtClass cc = pool.makeClass(mainClassName);
        cc.setSuperclass(pool.get(JavaPlugin.class.getName()));

        CtMethod onEnable = new CtMethod(CtClass.voidType, "onEnable", new CtClass[0], cc);
        onEnable.setModifiers(Modifier.PUBLIC);
        onEnable.setBody("{new " + Initialiser.class.getName() + "().initialise(this, " + typeElement.getQualifiedName() + ".class);}");

        cc.addMethod(onEnable);

        CtMethod onDisable = new CtMethod(CtClass.voidType, "onDisable", new CtClass[0], cc);
        onDisable.setModifiers(Modifier.PUBLIC);
        onDisable.setBody("{new " + Initialiser.class.getName() + "().disable(this, " + typeElement.getQualifiedName() + ".class);}");

        cc.addMethod(onDisable);
        
        cc.writeFile();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
