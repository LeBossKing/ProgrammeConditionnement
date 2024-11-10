//import org.apache.commons.math3.distribution.FDistribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int t1, t2;
        Scanner scan = new Scanner(System.in);

        System.out.println("*******************DEBUT*********************");

        System.out.print("Enter the number of elements in the groups: ");
        t1 = scan.nextInt();
        t2 = scan.nextInt();

        double[] groupeA = new double[t1];
        double[] groupeB = new double[t2];

        System.out.println("****************************************");

        for (int i = 0; i < groupeA.length; i++) {
            System.out.print("Enter the data number "+(i+1)+" of the first group: ");
            groupeA[i] = scan.nextDouble();
        }

        System.out.println("****************************************");

        for (int i = 0; i < groupeB.length; i++) {
            System.out.print("Enter the data number "+(i+1)+" of the second group: ");
            groupeB[i] = scan.nextDouble();
        }

        double moyA = calculeMoyenne(groupeA);
        double moyB = calculeMoyenne(groupeB);

        System.out.println("****************************************");

        System.out.println("Moyenne A: " + moyA);
        System.out.println("Moyenne B: " + moyB);

        double varA = calculeVariance(groupeA, moyA);
        double varB = calculeVariance(groupeB, moyB);

        System.out.println("****************************************");

        System.out.println("Variance A: " + varA);
        System.out.println("Variance B: " + varB);

        double ecrtA = calculeEcartType(varA);
        double ecrtB = calculeEcartType(varB);

        System.out.println("****************************************");

        System.out.println("Ecart-type A: " + ecrtA);
        System.out.println("Ecart-type B: " + ecrtB);

        System.out.println("****************************************");

        double testTc = calculeTestT(groupeA, groupeB, varA, varB);
        System.out.println("Test T calcule: " + testTc);

        System.out.println("****************************************");

        double testTh = evaluateFDistribution(groupeA, groupeB, varA, varB);
        System.out.println("Evaluation de test T par distribution de Fisher: " + testTh);

        System.out.println("****************************************");

        if(testTc < testTh) {
            System.out.println("On accepte H0, donc on peut considere que les variances des 2 groupes sont egales");

            System.out.println("****************************************");

            System.out.println("On calcule la variance commune: ");

            double varCommune = calucleVarianceCommune(groupeA, groupeB, varA, varB);
            System.out.println("Variance commune: "+varCommune);

            double ecartTypeCommune = calculeEcartType(varCommune);
//            System.out.println(ecartTypeCommune);

            System.out.println("****************************************");

            double testC = Math.abs(calculeTest(groupeA, groupeB, moyA, moyB, ecartTypeCommune));
            System.out.println("Calcule de test T: "+testC);

            System.out.println("****************************************");

            double testTth = evaluateTDistribution(groupeA, groupeB);
            System.out.println("Evaluate de test T par distribution Student: "+testTth);

            System.out.println("****************************************");

            if(testC > testTth){
                System.out.println("On regette H0, donc le programme avait un impact sur la performance des joueurs");
            }

        } else if (testTc > testTh) {
            System.out.println("On regette H0, donc on peut considere que les variances des 2 groupes sont differents");
        }

        System.out.println("******************FIN**********************");
    }

    public static double calculeMoyenne(double[] data){
        double sum = 0;

        for (double datum : data) {
            sum += datum;
        }

        return sum / data.length;
    }

    public static double calculeVariance(double[] data, double moyenne){
        double sum = 0;

        for(double dat: data){
            sum += Math.pow(dat-moyenne, 2);
        }

        return sum / data.length;
    }

    public static double calculeEcartType(double var){
        return Math.sqrt(var);
    }

    public static double calculeTestT(double[] data1, double[] data2, double var1, double var2){

        double numerator = 0;
        double denominator = 0;

        int n1 = 0;
        int n2 = 0;
        double v1 = 0;
        double v2 = 0;

        if (var1 > var2){
            n1 = data1.length;
            n2 = data2.length;
            v1 = var1;
            v2 = var2;
        }
        else if (var2 > var1){
            n1 = data2.length;
            n2 = data1.length;
            v1 = var2;
            v2 = var1;
        }
//        System.out.println("n1 = "+n1+", n2 = "+n2);
//        System.out.println("var1 = "+v1+", var2 = "+v2);

        numerator = (double) (n1 / (n1 - 1)) * v1;
        denominator = (double) (n2 / (n2- 1)) * v2;

        return numerator / denominator;
    }

    public static double evaluateFDistribution(double[] data1, double[] data2, double var1, double var2){

        int df1 = 0; int df2 = 0;
        double alpha = 0.05;

        if(var1 > var2){
            df1 = data1.length; df2 = data2.length;
        } else if (var2 > var1) {
            df1 = data2.length; df2 = data1.length;
        }

        FDistribution fDist = new FDistribution(df1-1, df2-1);

        return fDist.inverseCumulativeProbability(1-alpha);
    }

    public static double calucleVarianceCommune(double[] data1, double[] data2, double var1, double var2){
        int n1 = data1.length;
        int n2 = data2.length;

        return (((n1 - 1)*var1) + ((n2 - 1)* var2)) / ((n1 - 1) + (n2 -1));
    }

    public static double calculeTest(double[] data1, double[] data2, double moy1, double moy2
    , double ecartComm){

        int n1 = data1.length;
        int n2 = data2.length;

        return (moy1 - moy2) / (ecartComm * Math.sqrt(((double) 1 /n1 + (double) 1/n2)));
    }

    public static double evaluateTDistribution(double[] data1, double[] data2){

        int n1 = data1.length; int n2 = data2.length;
        double alpha = 0.05;

        TDistribution tDist = new TDistribution(n1+n2-2);

        return tDist.inverseCumulativeProbability(1-alpha / 2);
    }

}