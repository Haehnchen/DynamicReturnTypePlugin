package com.ptby.dynamicreturntypeplugin.returntype;

import com.jetbrains.php.lang.psi.elements.impl.MethodReferenceImpl;
import com.ptby.dynamicreturntypeplugin.callvalidator.MethodCallValidator;
import com.ptby.dynamicreturntypeplugin.config.ClassMethodConfig;

public class MethodCallTypeCalculator {


    private final MethodCallValidator methodCallValidator;
    private final CallReturnTypeCalculator callReturnTypeCalculator;


    public MethodCallTypeCalculator( MethodCallValidator methodCallValidator, CallReturnTypeCalculator callReturnTypeCalculator ) {
        this.methodCallValidator = methodCallValidator;
        this.callReturnTypeCalculator = callReturnTypeCalculator;
    }


    public String calculateFromMethodCall( ClassMethodConfig classMethodConfig, MethodReferenceImpl methodReference ) {
        if ( methodCallValidator
                .isValidMethodCall( methodReference, classMethodConfig ) ) {

            return callReturnTypeCalculator
                    .calculateTypeFromMethodParameter( methodReference, classMethodConfig.getParameterIndex() );
        }

        return null;
    }

}