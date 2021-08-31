package com.example.spring.batch.part4;

import com.example.spring.batch.part5.Orders;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.criterion.Order;

import javax.persistence.*;
import java.util.List;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String username;

    //private int totalAmount;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private List<Orders> ordersList;

    private LocalDate updatedDate;

    @Enumerated(EnumType.STRING)
    private Level level = Level.NORMAL;

    @Builder
    private User(String username, List<Orders> ordersList){
        this.username = username;
        this.ordersList = ordersList;
    }

    public boolean availableLevelUp() {
        return Level.availableLevelUp(this.getLevel(), this.getTotalAmount());
    }

    private int getTotalAmount() {
        return this.ordersList.stream()
                .mapToInt(Orders::getAmount)
                .sum();
    }

    public Level levelUp(){
        Level nextLevel = Level.getNextLevel(this.getTotalAmount());
        this.level = nextLevel;
        this.updatedDate = LocalDate.now();
        return nextLevel;
    }

    public enum Level{
        VIP(500_000, null),
        GOLD(500_000, VIP),
        SILVER(300_000, GOLD),
        NORMAL(200_000, SILVER);
        //NORMAL, SILVER, GOLD, VIP;

        private final int nextAmount;
        private final Level nextLevel;

        Level(int nextAmount, Level nextLevel) {
            this.nextAmount = nextAmount;
            this.nextLevel = nextLevel;
        }

        public static boolean availableLevelUp(Level level, int totalAmount) {
              if(Objects.isNull(level)){
                  return false;
              }
              if(Objects.isNull(level.nextLevel)){
                  return false;
              }
              return totalAmount >= level.nextAmount;
        }

        private static Level getNextLevel(int totalAmount) {
            if(totalAmount >= Level.VIP.nextAmount){
                return VIP;
            }
            if(totalAmount >= Level.GOLD.nextAmount){
                return GOLD;
            }
            if(totalAmount >= Level.SILVER.nextAmount){
                return SILVER;
            }
            return NORMAL;

        }
    }



}
