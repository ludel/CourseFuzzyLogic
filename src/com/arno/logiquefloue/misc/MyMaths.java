package com.arno.logiquefloue.misc;

import java.util.function.DoubleBinaryOperator;

public class MyMaths {
	public static double MIN(double a, double b) {
		return a<b?a:b;
	}
	public static double MAX(double a, double b) {
		return a>b?a:b;
	}
	public static int SIGNE(double v){
	    if(v<0)
	        return -1;
	    else if (v>0) {
	        return 1;
	    }
	    else
	        return 0;
	}
	public static double ABS(double v) {
		return v<0?-v:v;
	}
	/**
	 * Exemples d'appels de cette méthode :
	 * - System.out.println(MyMaths.oper(MyMaths::MIN, 17.2, 13.5));
	 * - System.out.println(MyMaths.oper(MyMaths::Max, 17.2, 13.5));
	 */
	public static double oper(DoubleBinaryOperator operator,double a, double b) {
		return operator.applyAsDouble(a, b);
	}
}
