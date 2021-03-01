# Firebase Cloud Firestore
### 요구 사항
* 데이터 베이스에는 상품(Items) collection이 있다.
* Items collection의 document에는 name, price, cart 필드가 있다.
* MainActivity에는 상품 목록을 보여주는 RecycelrView만 있다.
* 목록에서 상품을 선택하면 자세히 보기(Item Activity) 시작
* ItemActivity에는
  1. 상품 이름, 가격
  2. 장바구니에 넣기/빼기 버튼
  3. 장바구니 버튼을 누르면 장바구니에 상품 넣음
  4. 장바구니에 이미 있는 상품은 빼기 버튼으로 동작
  5. MainActivity로 되돌아가는 Up 버튼
<br><br><br><br> 
 
 
 1. 상품을 선택하면 해당 상품의 자세히 보기가 나온다. <br>
 ![ezgif com-gif-maker (7)](https://user-images.githubusercontent.com/68210266/109488087-6fa40480-7ac8-11eb-9cba-577b75c263a1.gif)
 <br><br><br><br>
 
 2. 해당 상품의 true/false 값(상품 넣기/빼기)을 변경한다. => firebase에서 업데이트가 되지 않는다.
![ezgif com-gif-maker (8)](https://user-images.githubusercontent.com/68210266/109488090-70d53180-7ac8-11eb-8b10-23273ad381ef.gif)
