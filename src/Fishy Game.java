import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color; // general colors (as triples of red,green,blue values)
// and predefined colors (Red, Green, Yellow, Blue, Black, White)
import java.util.Random;

// Interface for predicate-objects with siganture [T -> Boolean]
interface IPred<T> {
  boolean apply(T t);
}

class RemoveOffScreen implements IPred<Fish> {
  public boolean apply(Fish f) {
    return !f.isOffscreen(1000, 1000, f);
  }
}

// Interface for comparator-objects with siganture [T -> Boolean]
interface IComparator<T> {
  boolean apply(T t1, T t2);
}

class IsEatenBy implements IComparator<Fish> {
  public boolean apply(Fish f1, Fish f2) {
    return f1.fishCollision(f2) && f1.canFishEat(f2);
  }
}

class IsNotEatenBy implements IComparator<Fish> {
  public boolean apply(Fish f1, Fish f2) {
    return !f2.fishCollision(f1);
  }
}

class IsEaten implements IComparator<Fish> {
  public boolean apply(Fish f1, Fish f2) {
    return f1.fishCollision(f2);
  }
}

// Interface for one-argument function-object with signature [A -> R]
interface IFunc<A, R> {
  R apply(A arg);
}

class MoveAll implements IFunc<Fish, Fish> {
  public Fish apply(Fish f) {
    return f.move();
  }
}

// Interface for two-argument function-objects with signature [A1, A2 -> R]
interface IFunc2<A1, A2, R> {
  R apply(A1 arg1, A2 arg2);
}

class PlaceAll implements IFunc2<Fish, WorldScene, WorldScene> {
  public WorldScene apply(Fish f, WorldScene ws) {
    return f.place(ws);
  }
}

class Utils {

  // returns a list of identities that pass through a one argument function of
  // length n
  <U> IList<U> buildlist(int n, IFunc<Integer, U> func) {
    if (n == 0) {
      return new MtList<U>();
    }
    else {
      return new ConsList<U>(func.apply(n), buildlist(n, func));
    }
  }

  // returns a list of T of length n
  <T> IList<T> makelist(int n, T t) {
    if (n == 0) {
      return new MtList<T>();
    }
    else {
      return new ConsList<T>(t, makelist(n, t));
    }
  }

}

// represent a list
interface IList<T> {

  // returns the length of the given list of objects
  int length();

  // returns only the objects of a given list that pass a certain predicate
  IList<T> filter(IPred<T> pred);

  IList<T> filteracc(IComparator<T> comp, T t);

  // return the list of objects rearranged according to the given comparator
  IList<T> sort(IComparator<T> comp);

  // return the list of objects with the new object arranged according to the
  // given comparator
  IList<T> insert(T t, IComparator<T> comp);

  // returns the list of objects after it has passed through a one-argument
  // function
  <U> IList<U> map(IFunc<T, U> func);

  // returns a boolean determining whether or not all objects in the list pass a
  // certain predicate
  boolean andmap(IPred<T> func);

  // returns a boolean determining whether or not any objects in the list pass a
  // certain predicate
  boolean ormap(IPred<T> func);

  boolean ormapacc(IComparator<T> comp, T t);

  // returns the base value of a list of objects after it has passed through a
  // two-argument function from right to left
  <U> U foldr(IFunc2<T, U, U> func, U base);

  // returns the base value of a list of objects after it has passed through a
  // two argument function from left to right
  <U> U foldl(IFunc2<T, U, U> func, U base);

  // returns a list of objects containing the values of two list of objects
  IList<T> append(IList<T> that);

  // returns the nth object in the list, or null if no such object exists in said
  // list
  Object listref(int n);

  // returns the nth tail of objects , or an empty list if no such tail exists in
  // said list
  IList<T> listtail(int n);

  // returns the given list of objects in a reverse order
  IList<T> reverse();

  IList<T> swap(IList<T> that);

  // returns a list of objects containing the result of two lists passing through
  // a two argument function
  <U, R> IList<R> convolve(IList<U> that, IFunc2<T, U, R> converter);

  <U, R> IList<R> convolveHelper(U thatFirst, IList<U> thatRest, IFunc2<U, T, R> converter);

  T getFirst();

}

// represent a empty list
class MtList<T> implements IList<T> {

  // returns the length of the given list of objects
  public int length() {
    return 0;
  }

  // returns only the objects of a given list that pass a certain predicate
  public IList<T> filter(IPred<T> pred) {
    return this;
  }

  public IList<T> filteracc(IComparator<T> comp, T t) {
    return this;
  }

  // return the list of objects rearranged according to the given comparator
  public IList<T> sort(IComparator<T> comp) {
    return this;
  }

  // return the list of objects with the new object arranged according to the
  // given comparator
  public IList<T> insert(T t, IComparator<T> comp) {
    return new ConsList<T>(t, this);
  }

  // returns the list of objects after it has passed through a one-argument
  // function
  public <U> IList<U> map(IFunc<T, U> func) {
    return new MtList<U>();
  }

  // returns a boolean determining whether or not all objects in the list pass a
  // certain predicate
  public boolean andmap(IPred<T> func) {
    return true;
  }

  // returns a boolean determining whether or not any objects in the list pass a
  // certain predicate
  public boolean ormap(IPred<T> func) {
    return false;
  }

  public boolean ormapacc(IComparator<T> comp, T t) {
    return false;
  }

  // returns the base value of a list of objects after it has passed through a
  // two-argument function from right to left
  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return base;
  }

  // returns the base value of a list of objects after it has passed through a
  // two-argument function from left to right
  public <U> U foldl(IFunc2<T, U, U> func, U base) {
    return base;
  }

  // returns a list of objects containing the values of two list of objects
  public IList<T> append(IList<T> that) {
    return that;
  }

  // returns the nth object in the list, or null if no such object exists in said
  // list
  public Object listref(int n) {
    return null;
  }

  // returns the nth tail of objects , or an empty list if no such tail exists in
  // said list
  public IList<T> listtail(int n) {
    return this;
  }

  // returns the given list of objects in a reverse order
  public IList<T> reverse() {
    return this;
  }

  public IList<T> swap(IList<T> that) {
    return that;
  }

  // returns a list of objects containing the result of two lists passing through
  // a two argument function
  public <U, R> IList<R> convolve(IList<U> that, IFunc2<T, U, R> func) {
    return new MtList<R>();
  }

  public <U, R> IList<R> convolveHelper(U thatFirst, IList<U> thatRest, IFunc2<U, T, R> func) {
    return new MtList<R>();

  }

  public T getFirst() {
    return null;
  }
}

// represent a non-empty list
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // returns the length of the given list of objects
  public int length() {
    return 1 + this.rest.length();
  }

  // returns only the objects of a given list that pass a certain predicate
  public IList<T> filter(IPred<T> pred) {
    if (pred.apply(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  public IList<T> filteracc(IComparator<T> comp, T t) {
    if (comp.apply(t, this.first)) {
      return new ConsList<T>(this.first, this.rest.filteracc(comp, t));
    }
    else {
      return this.rest.filteracc(comp, t);
    }
  }

  // return the list of objects rearranged according to the given comparator
  public IList<T> sort(IComparator<T> comp) {
    return this.rest.sort(comp).insert(this.first, comp);
  }

  // return the list of objects with the new object arranged according to the
  // given comparator
  public IList<T> insert(T t, IComparator<T> comp) {
    if (comp.apply(this.first, t)) {
      return new ConsList<T>(this.first, this.rest.insert(t, comp));
    }
    else {
      return new ConsList<T>(t, this);
    }
  }

  // returns the list of objects after it has passed through a one-argument
  // function
  public <U> IList<U> map(IFunc<T, U> func) {
    return new ConsList<U>(func.apply(this.first), this.rest.map(func));
  }

  // returns a boolean determining whether or not all objects in the list pass a
  // certain predicate
  public boolean andmap(IPred<T> func) {
    return func.apply(this.first) && this.rest.andmap(func);
  }

  // returns a boolean determining whether or not all objects in the list pass a
  // certain predicate
  public boolean ormap(IPred<T> func) {
    return func.apply(this.first) || this.rest.ormap(func);
  }

  public boolean ormapacc(IComparator<T> comp, T t) {
    return comp.apply(t, this.first) || this.rest.ormapacc(comp, t);
  }

  // returns the base value of a list of objects after it has passed through a
  // two-argument function from right to left
  public <U> U foldr(IFunc2<T, U, U> func, U base) {
    return func.apply(this.first, this.rest.foldr(func, base));
  }

  // returns the base value of a list of objects after it has passed through a
  // two-argument function from left to right
  public <U> U foldl(IFunc2<T, U, U> func, U base) {
    return this.reverse().foldr(func, base);
  }

  // returns a list of objects containing the values of two list of objects
  public IList<T> append(IList<T> that) {
    return new ConsList<T>(this.first, this.rest.append(that));
  }

  // returns the nth object in the list, or null if no such object exists in said
  // list
  public Object listref(int n) {
    if (n == 1) {
      return this.first;
    }
    else {
      return this.rest.listref(n - 1);
    }
  }

  // returns the nth tail of objects , or an empty list if no such tail exists in
  // said list
  public IList<T> listtail(int n) {
    if (n == 1) {
      return this;
    }
    else {
      return this.rest.listtail(n - 1);
    }
  }

  // returns the given list of objects in a reverse order
  public IList<T> reverse() {
    return this.rest.swap(new ConsList<T>(this.first, new MtList<T>()));
  }

  public IList<T> swap(IList<T> that) {
    return this.rest.swap(new ConsList<T>(this.first, that));
  }

  // returns a list of objects containing the result of two lists passing through
  // a two argument function
  public <U, R> IList<R> convolve(IList<U> that, IFunc2<T, U, R> func) {
    return that.convolveHelper(this.first, this.rest, func);
  }

  public <U, R> IList<R> convolveHelper(U thatFirst, IList<U> thatRest, IFunc2<U, T, R> func) {
    return new ConsList<R>(func.apply(thatFirst, this.first), thatRest.convolve(this.rest, func));
  }

  public T getFirst() {
    return this.first;
  }

}

class APosn extends Posn {
  APosn(int x, int y) {
    super(x, y);
  }

  public APosn add(APosn that) {
    return new APosn((this.x + that.x), (this.y + that.y));
  }

  public boolean isOffscreen(int width, int height, Fish f) {
    return this.x < (0 - (25 * f.level)) || this.x > (width + (25 * f.level));
  }

  WorldScene placeImageOnScene(WorldScene ws, WorldImage wi) {
    return ws.placeImageXY(wi, this.x, this.y);
  }

}

// to represent a fish
class Fish {

  APosn coordinates, velocity;
  int level, experience, score;
  String facing;
  Color color;

  Fish(APosn coordinates, APosn velocity, int level, int experience, int score, String facing,
      Color color) {
    this.coordinates = coordinates;
    this.velocity = velocity;
    this.level = level;
    this.experience = experience;
    this.score = score;
    this.facing = facing;
    this.color = color;
  }

  boolean fishCollision(Fish that) {
    return ((this.coordinates.x - (25 * this.level)) <= (that.coordinates.x + (25 * that.level)))
        && ((this.coordinates.x + (25 * this.level)) >= (that.coordinates.x - (25 * that.level)))
        && ((this.coordinates.y - (15 * this.level)) <= (that.coordinates.y + (15 * that.level)))
        && ((this.coordinates.y + (15 * this.level)) >= (that.coordinates.y - (15 * that.level)));
  }

  boolean canFishEat(Fish that) {
    return this.level >= that.level;
  }

  Fish eatFish(Fish that) {
    if (this.experience + that.experience >= this.level * 100) {
      return new Fish(this.coordinates, this.velocity, this.level + 1,
          (this.experience + that.experience) - (this.level * 100), this.score + that.experience,
          this.facing, this.color);
    }
    else {
      return new Fish(this.coordinates, this.velocity, this.level,
          this.experience + that.experience, this.score + that.experience, this.facing, this.color);
    }
  }

  Fish movePlayer() {
    if (this.coordinates.add(this.velocity).x >= 1000 + (this.level * 25)) {
      return new Fish(new APosn(0 - (this.level * 25), this.coordinates.add(this.velocity).y),
          this.velocity, this.level, this.experience, this.score, this.facing, this.color);
    }
    else if (this.coordinates.add(this.velocity).x <= 0 - (this.level * 25)) {
      return new Fish(new APosn(1000 + (this.level * 25), this.coordinates.add(this.velocity).y),
          this.velocity, this.level, this.experience, this.score, this.facing, this.color);
    }
    else if (this.coordinates.add(this.velocity).y >= 1000 - (this.level * 15)) {
      return new Fish(new APosn(this.coordinates.add(this.velocity).x, 1000 - (this.level * 15)),
          this.velocity, this.level, this.experience, this.score, this.facing, this.color);
    }
    else if (this.coordinates.add(this.velocity).y <= 0 + (this.level * 15)) {
      return new Fish(new APosn(this.coordinates.add(this.velocity).x, 0 + (this.level * 15)),
          this.velocity, this.level, this.experience, this.score, this.facing, this.color);
    }
    else {
      return new Fish(this.coordinates.add(this.velocity), this.velocity, this.level,
          this.experience, this.score, this.facing, this.color);
    }
  }

  Fish move() {
    if (this.coordinates.add(this.velocity).y >= 1000 - (this.level * 15)) {
      return new Fish(new APosn(this.coordinates.add(this.velocity).x, 1000 - (this.level * 15)),
          this.velocity, this.level, this.experience, this.score, this.facing, this.color);
    }
    else if (this.coordinates.add(this.velocity).y <= 0 + (this.level * 15)) {
      return new Fish(new APosn(this.coordinates.add(this.velocity).x, 0 + (this.level * 15)),
          this.velocity, this.level, this.experience, this.score, this.facing, this.color);
    }
    else {
      return new Fish(this.coordinates.add(this.velocity), this.velocity, this.level,
          this.experience, this.score, this.facing, this.color);
    }
  }

  boolean isOffscreen(int width, int height, Fish f) {
    return this.coordinates.isOffscreen(width, height, f);
  }

  WorldImage draw() {
    WorldImage blackPixel = new RectangleImage(this.level * 10, this.level * 10, OutlineMode.SOLID,
        this.color);
    WorldImage whitePixel = new RectangleImage(this.level * 10, this.level * 10,
        OutlineMode.OUTLINE, Color.WHITE);

    WorldImage fishSides1 = new BesideImage(blackPixel, blackPixel, blackPixel, whitePixel,
        blackPixel);
    WorldImage fishMiddle1 = new BesideImage(blackPixel, whitePixel, blackPixel, blackPixel,
        whitePixel);

    WorldImage fish1 = new AboveImage(fishSides1, fishMiddle1, fishSides1);

    WorldImage fishSides2 = new BesideImage(blackPixel, whitePixel, blackPixel, blackPixel,
        blackPixel);
    WorldImage fishMiddle2 = new BesideImage(whitePixel, blackPixel, blackPixel, whitePixel,
        blackPixel);

    WorldImage fish2 = new AboveImage(fishSides2, fishMiddle2, fishSides2);

    if (this.facing == "left") {
      return fish1;
    }
    else {
      return fish2;
    }
  }

  WorldScene place(WorldScene scene) {
    return this.coordinates.placeImageOnScene(scene, this.draw());
  }

  Fish moves(int x, int y, String face) {
    if (face == "left") {
      return new Fish(this.coordinates, (new APosn(x / this.level, y / this.level)), this.level,
          this.experience, this.score, "left", this.color);
    }
    else if (face == "right") {
      return new Fish(this.coordinates, (new APosn(x / this.level, y / this.level)), this.level,
          this.experience, this.score, "right", this.color);
    }
    else {
      return new Fish(this.coordinates, (new APosn(x / this.level, y / this.level)), this.level,
          this.experience, this.score, this.facing, this.color);
    }
  }

}

// to represent a fishy game
class FishGame extends World {

  Fish player;
  IList<Fish> enemies;
  Random rand;

  FishGame(Fish player, IList<Fish> enemies, Random rand) {
    this.player = player;
    this.enemies = enemies;
    this.rand = rand;
  }

  FishGame(Fish player, IList<Fish> enemies) {
    this.player = player;
    this.enemies = enemies;
    this.rand = new Random();
  }

  FishGame() {
    this(new Fish((new APosn(500, 500)), (new APosn(0, 0)), 1, 0, 0, "left", Color.BLACK),
        new MtList<Fish>(), new Random());
  }

  public boolean canBeEaten() {
    return this.enemies.ormapacc(new IsEatenBy(), this.player);
  }

  public Fish eatOrEaten() {
    if (this.canBeEaten()) {
      return this.player.eatFish(enemies.filteracc(new IsEatenBy(), this.player).getFirst());
    }
    else if (this.enemies.ormapacc(new IsEaten(), this.player)) {
      return new Fish(this.player.coordinates, this.player.velocity, this.player.level,
          this.player.experience, this.player.score, this.player.facing, Color.RED);
    }
    else {
      return this.player;
    }
  }

  public IList<Fish> eatenOrEat() {
    if (this.canBeEaten()) {
      return new FishGame(
          this.player,
          this.enemies.filteracc(new IsNotEatenBy(), this.player)).spawnEnemy();
    }
    else {
      return this.spawnEnemy();
    }
  }

  public IList<Fish> spawnEnemy() {
    if (this.rand.nextInt(10) == 1) {
      return this.addEnemy();
    }
    else {
      return this.enemies;
    }
  }

  public IList<Fish> addEnemy() {
    int randomFish = this.rand.nextInt(5) + 1;
    if (this.rand.nextInt(2) == 1) {
      return new ConsList<Fish>(
          new Fish((new APosn(1000 + (25 * randomFish), this.rand.nextInt(1000))),
              (new APosn(-60 / randomFish, 0)), randomFish, randomFish * 10, 0, "left",
              Color.BLACK),
          this.enemies);
    }
    else {
      return new ConsList<Fish>(
          new Fish((new APosn(0 - (25 * randomFish), this.rand.nextInt(1000))),
              (new APosn(60 / randomFish, 0)), randomFish, randomFish * 10, 0, "right",
              Color.BLACK),
          this.enemies);
    }
  }

  public WorldScene makeScene() {
    return new ConsList<Fish>(this.player, enemies).foldr(new PlaceAll(), this.getEmptyScene());
  }

  public WorldEnd worldEnds() {
    if (this.player.level == 6) {
      return new WorldEnd(true,
          new FishGame(
              new Fish(this.player.coordinates, this.player.velocity, this.player.level,
                  this.player.experience, this.player.score, this.player.facing, Color.GREEN),
              this.enemies).makeScene());
    }
    else if (this.player.color.equals(Color.RED)) {
      return new WorldEnd(true, this.makeScene());
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  public World onKeyEvent(String key) {
    if (key.equals("up")) {
      return new FishGame(this.player.moves(0, -60, "up"), this.enemies);
    }
    else if (key.equals("down")) {
      return new FishGame(this.player.moves(0, 60, "down"), this.enemies);
    }
    else if (key.equals("left")) {
      return new FishGame(this.player.moves(-60, 0, "left"), this.enemies);
    }
    else if (key.equals("right")) {
      return new FishGame(this.player.moves(60, 0, "right"), this.enemies);
    }
    else {
      return this;
    }
  }

  public World onTick() {
    return new FishGame(this.eatOrEaten().movePlayer(),
        this.eatenOrEat().map(new MoveAll()).filter(new RemoveOffScreen()));
  }

}

class YourWorld {
  Random rand;

  // The constructor for use in "real" games
  YourWorld() {
    this(new Random());
  }

  // The constructor for use in testing, with a specified Random object
  YourWorld(Random rand) {
    this.rand = rand;
  }

}

class ExamplesFish {

  FishGame game = new FishGame();
  FishGame game2 = new FishGame(
      new Fish((new APosn(500, 500)), (new APosn(0, 0)), 1, 0, 0, "left", Color.BLACK),
      new MtList<Fish>());

  boolean testGame(Tester t) {
    return game2.bigBang(1000, 1000, 0.1);
  }

  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(game2.onKeyEvent("left"),
        new FishGame(
            new Fish((new APosn(500, 500)), (new APosn(-5, 0)), 1, 0, 0, "left", Color.BLACK),
            new MtList<Fish>()));
  }

}