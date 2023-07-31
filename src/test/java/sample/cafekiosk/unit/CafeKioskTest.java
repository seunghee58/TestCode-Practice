package sample.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 담긴 음료 수 : " + cafeKiosk.getBerverages().size());
        System.out.println(">>> 담긴 음료 : " + cafeKiosk.getBerverages().get(0).getName());
    }

    @DisplayName("음료를 1개를 추가하면 주문 목록에 담긴다.")
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThat(cafeKiosk.getBerverages()).hasSize(1);
//        assertThat(cafeKiosk.getBerverages().size()).isEqualTo(1); // 위와 같음
        assertThat(cafeKiosk.getBerverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("여러 잔의 음료를 주문 목록에 추가한다.")
    @Test
    void  addSeveralBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 2);

        assertThat(cafeKiosk.getBerverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBerverages().get(1)).isEqualTo(americano);
    }

    @DisplayName("음료를 주문 목록에 추가하지 않고 주문하면 에러가 발생한다.")
    @Test
    void  addZeroBeverages() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(americano,0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    @DisplayName("선택한 음료를 주문 목록에서 삭제한다.")
    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBerverages()).hasSize(1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBerverages()).isEmpty();

    }

    @DisplayName("주문 목록을 전부 삭제한다.")
    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);
        assertThat(cafeKiosk.getBerverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBerverages()).isEmpty();
    }

    @DisplayName("주문 목록의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrice() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        int totalPrice = cafeKiosk.calculateTotalPrice();

        assertThat(totalPrice).isEqualTo(8500);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder();

        assertThat(order.getBerverages()).hasSize(1);
        assertThat(order.getBerverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("주문 가능 시간에는 주문이 생성된다.")
    @Test
    void createOrderWhitCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023,6,23, 10,0));

        assertThat(order.getBerverages()).hasSize(1);
        assertThat(order.getBerverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("주문 가능 시간 외에는 주문을 생성할 수 없다.")
    @Test
    void createOrderOutsideCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);

        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 6, 23, 9, 59)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 시간이 아닙니다. 관리자에게 문의하세요.");
    }

}