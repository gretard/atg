package edu.ktu.atg.outputter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.TypeDeclaration;

import edu.ktu.atg.common.execution.GenerationData;

public final class OuputGenerator {

    private static final Logger LOGGER = Logger.getLogger(OuputGenerator.class.getName());

    private final JunitTestsGenerator generator = new JunitTestsGenerator();

    public void generate(List<GenerationData> items, String outDir) {
        for (final GenerationData data : items) {
            try {
                final CompilationUnit cu = generator.generate(data);
                final NodeList<TypeDeclaration<?>> types = cu.getTypes();
                if (types == null || types.isEmpty()) {
                    continue;
                }
                final Optional<String> typeDeclaration = types.get(0).getFullyQualifiedName();
                if (!typeDeclaration.isPresent()) {
                    continue;
                }
                final File file = new File(outDir, typeDeclaration.get().replace('.', File.separatorChar) + ".java");

                FileUtils.write(file, cu.toString(), Charset.defaultCharset());
                LOGGER.info(() -> "Saved results to: " + file);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Unexpected error generating test", e);
            }

        }
    }
}
