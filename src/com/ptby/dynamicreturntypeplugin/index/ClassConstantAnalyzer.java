package com.ptby.dynamicreturntypeplugin.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.ptby.dynamicreturntypeplugin.signatureconversion.CustomMethodCallSignature;
import com.ptby.dynamicreturntypeplugin.signatureconversion.SignatureMatcher;

import java.util.Collection;

public class ClassConstantAnalyzer {
    private static final SignatureMatcher signatureMatcher = new SignatureMatcher();


    public boolean verifySignatureIsClassConstant( String signature ) {
        return signatureMatcher.verifySignatureIsClassConstantFunctionCall( signature );
    }


    public String getClassNameFromConstantLookup( String classConstant, Project project ) {
        String[] constantParts = classConstant.split( "((#*)K#C|\\.|\\|\\?)" );

        if ( constantParts.length < 2 ) {
            return null;
        }

        if ( constantParts.length == 2 ) {
            return constantParts[ 1 ];
        }

        String className = constantParts[ 1 ];
        String constantName = constantParts[ 2 ];

        PhpIndex phpIndex = PhpIndex.getInstance( project );
        Collection<PhpClass> classesByFQN = phpIndex.getClassesByFQN( className );
        for ( PhpClass phpClass : classesByFQN ) {
            Collection<Field> fields = phpClass.getFields();
            for ( Field field : fields ) {
                if ( field.isConstant() && field.getName().equals( constantName ) ) {
                    PsiElement defaultValue = field.getDefaultValue();
                    if ( defaultValue == null ) {
                        return null;
                    }
                    String constantText = defaultValue.getText();
                    if ( constantText.equals( "__CLASS__" ) ) {
                        return className;
                    }
                }
            }
        }

        return null;
    }

}
