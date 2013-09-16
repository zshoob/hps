
import java.util.Arrays;

public class Mint{
  private static double N;
  private static int[] upperDenomination = new int[]{1, 5, 10, 25, 50};

  private static double upperBound = 0;
  private static int[] optiDenomination = new int[5];
  private static double optiTotal = 0;
  private static int[] exactChangeNo = new int[100];

  private static double upperTotalForExchange = 0;
  private static int[] optiDenominationForExchange = new int[5];
  private static double optiTotalForExchange = 0;
  private static int[] exchangeNo = new int[101];


  private void calculateUpperTotal(){
    exactChangeNo[0] = 0;
    for(int i = 1; i < 100; i++){
      int tmpMin = Integer.MAX_VALUE;
      for(int j = 0; j < 5; j++){
        if(upperDenomination[j] <= i && tmpMin > exactChangeNo[i - upperDenomination[j]] + 1){
          tmpMin = exactChangeNo[i - upperDenomination[j]] + 1;
        }
      }
      exactChangeNo[i] = tmpMin;
      if(i % 5 == 0){
        upperBound += exactChangeNo[i] * N;
      }else{
        upperBound += exactChangeNo[i];
      }
    }
  }

  private double calculateExactChangeNo(int[] denomination){
    double totalChange = 0.0;
    exactChangeNo[0] = 0;
    for(int i = 1; i < 100; i++){
      int tmpMin = Integer.MAX_VALUE;
      for(int j = 0; j < 5; j++){
        if(denomination[j] <= i && tmpMin > exactChangeNo[i - denomination[j]] + 1){
          tmpMin = exactChangeNo[i - denomination[j]] + 1;
        }
      }
      exactChangeNo[i] = tmpMin;
      if(i % 5 == 0){
        totalChange += exactChangeNo[i] * N;
      }else{
        totalChange += exactChangeNo[i];
      }
      if(totalChange > upperBound){
        return -1.0;
      }
    }
    return totalChange;
  }

  private void getOptimDenominationForExact(){
    calculateUpperTotal();
    int[] tmpDenomination;
    double minTotal = Double.MAX_VALUE;
    for(int i = 2; i < 96; i++){
      for(int j = i + 1; j < 97; j++){
        for(int k = j + 1; k < 98; k++){
          for(int l = k + 1; l < 99; l++){
            tmpDenomination = new int[]{1, i, j, k, l};
            double tmpTotal = calculateExactChangeNo(tmpDenomination);
            if(tmpTotal > 0 && minTotal > tmpTotal){
              minTotal = tmpTotal;
              optiDenomination = tmpDenomination;
            }
          }
        }
      }
    }
    optiTotal = minTotal;
  }

  

  private void calculateUpperTotalForExchange(){
    exchangeNo[0] = 0;
    for(int i = 1; i < 100; i++){
      int tmpMin = Integer.MAX_VALUE;
      for(int j = 0; j < 5; j++){
        if(upperDenomination[j] <= i && tmpMin > exchangeNo[i - upperDenomination[j]] + 1){
          tmpMin = exchangeNo[i - upperDenomination[j]] + 1;
        }
      }
      exchangeNo[i] = tmpMin;
    }

    exchangeNo[100] = 0;
    for(int i = 99; i >= 0; i--){
      int tmpMin = Integer.MAX_VALUE;
      for(int j = 0; j < 5; j++){
        if((upperDenomination[j] <= (100 - i)) && tmpMin > exchangeNo[i + upperDenomination[j]] + 1){
          tmpMin = exchangeNo[i + upperDenomination[j]] + 1;
        }
      }
      if(exchangeNo[i] > tmpMin){
        exchangeNo[i] = tmpMin;
      }

    }

    for(int i = 0; i < 100; i++){
      if(i > 50){
        exchangeNo[i] = exchangeNo[100 - i];
      }
      if(i % 5 == 0){
        upperTotalForExchange += exchangeNo[i] * N;
      }else{
        upperTotalForExchange += exchangeNo[i];
      }
    }
  }

  private double calculateExchangeNo(int[] denomination){
    double totalExchange = 0;
    exchangeNo[0] = 0;
    for(int i = 1; i < 100; i++){
      int tmpMin = Integer.MAX_VALUE;
      for(int j = 0; j < 5; j++){
        if(denomination[j] <= i && tmpMin > exchangeNo[i - denomination[j]] + 1){
          tmpMin = exchangeNo[i - denomination[j]] + 1;
        }
      }
      exchangeNo[i] = tmpMin;
    }

    exchangeNo[100] = 0;
    for(int i = 99; i >= 0; i--){
      int tmpMin = Integer.MAX_VALUE;
      for(int j = 0; j < 5; j++){
        if((denomination[j] <= (100 - i)) && tmpMin > exchangeNo[i + denomination[j]] + 1){
          tmpMin = exchangeNo[i + denomination[j]] + 1;
        }
      }
      if(exchangeNo[i] > tmpMin){
        exchangeNo[i] = tmpMin;
      }
    }

    for(int i = 0; i < 100; i++){
      if(i > 50){
        exchangeNo[i] = exchangeNo[100 - i];
      }
      if(i % 5 == 0){
        totalExchange += exchangeNo[i] * N;
      }else{
        totalExchange += exchangeNo[i];
      }
      if(totalExchange > upperTotalForExchange){
        return -1.0;
      }
    }
    return totalExchange;
  }

  private void getOptimDenominationForExchange(){
    calculateUpperTotalForExchange();
    int[] tmpDenomination;
    double minTotal = Double.MAX_VALUE;
    for(int m = 1; m < 46; m++){
      for(int i = m + 1; i < 47; i++){
        for(int j = i + 1; j < 48; j++){
          for(int k = j + 1; k < 49; k++){
            for(int l = k + 1; l < 50; l++){
              tmpDenomination = new int[]{m, i, j, k, l};
              double tmpTotal = calculateExchangeNo(tmpDenomination);
              if(tmpTotal > 0 && minTotal > tmpTotal){
                minTotal = tmpTotal;
                optiDenominationForExchange = tmpDenomination;
              }
            }
          }
        }
      }
    }
    optiTotalForExchange = minTotal;
  }

  public static void main(String[] args){
    if(args.length != 2){
      throw new RuntimeException("Please enter right parameters!");
    }
    // N value
    N = Double.parseDouble(args[0]);

    // The choice for problem
    int choice = Integer.parseInt(args[1]);

    Mint mint = new Mint();
    System.out.println("TEAM");
    if(choice == 1){
      mint.getOptimDenominationForExact();
      System.out.printf("%d %d %d %d %d %n", optiDenomination[0], 
        optiDenomination[1], optiDenomination[2], optiDenomination[3], optiDenomination[4]);
      System.out.printf("%,.2f %n", optiTotal);
    }else if(choice == 2){
      mint.getOptimDenominationForExchange();
      System.out.printf("%d %d %d %d %d %n", optiDenominationForExchange[0], 
        optiDenominationForExchange[1], optiDenominationForExchange[2], optiDenominationForExchange[3], optiDenominationForExchange[4]);
      System.out.printf("%,.2f %n", optiTotalForExchange);
    }
  }

}

