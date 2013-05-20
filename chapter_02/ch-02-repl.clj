(use 'clojure.repl)

;; ------------ Week 3 -------------- ;;

(assoc {:a 1} :a 2222)

(merge {:a 1, :b 2, :c 3}
       {:a 111, :b 222, :d 4}
       {:b "two"})

(def Point
  (fn [x y]
    {:x x
     :y y}))

(def x :x)
(def y :y)

(def shift
(fn [this xinc yinc]
       (Point (+ (x this) xinc)
              (+ (y this) yinc))))

(shift (Point 1 200) -1 -200)

;; Exercise 1: Implement add.

(def add
  (fn [this that]
    (Point (+ (x this) (x that))
           (+ (y this) (y that))
    )
  )
)

(add (Point 100 100) (Point -50 -50))

(def add
  (fn [this that]
    (shift this (x that) (y that))
  )
)

(add (Point 100 100) (Point -50 -50))

;; Exercise 2: A new operator

(def make
  (fn [f & args]
     (apply f args)
  )
)

(make Point 1 2)


;; Exercise 3

(def Triangle
  (fn [x y z]
    {:x x
     :y y
     :z z}))

(def z :z)

(def right-triangle
  (Triangle (Point 1 2) (Point 2 3) (Point 3 1)))

(def equal-right-triangle
  (Triangle (Point 2 3) (Point 3 1) (Point 1 2)))


;; ------------ Week 4 -------------- ;;

;; Exercise 4.1

(def Point (fn [x y]
{;; initializing instance variables
 :x x
 :y y

 ;; Metadata
 :__class_symbol__ 'Point
 :__methods__ {
    :class :__class_symbol__

    :get-x
    (fn [] x)

    :get-y
    (fn [] y)

    :shift
    (fn [this xinc yinc]
      (make Point (+ (:x this) xinc)
                  (+ (:y this) yinc)))}}))

(def send-to
  (fn [object message & args]
    (apply (message (:__methods__ object))
           object args)))

(def shift
(fn [this xinc yinc]
       (Point (+ (x this) xinc)
              (+ (y this) yinc))))

(def myPoint (Point 10 10))
(shift myPoint 5 1)

;; ------------ Week 5 -------------- ;;

;; Reading only

;; ------------ Week 6 -------------- ;;

(def Point
{
  :__own_symbol__ 'Point
  :__instance_methods__
  {
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    :class :__class_symbol__
    :shift (fn [this xinc yinc]
             (make Point (+ (:x this) xinc)
                         (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

;; Exercise 1

(defn get-method-from [message class]
  (message (:__instance_methods__ class))
)

(defn apply-message-to [class instance message args]
  (let [method (get-method-from message class)]
    (apply method instance args)
  )
)

(defn create-seeded-instance [class & args]
  {:__class_symbol__ (:__own_symbol__ class)}
)

(defn make [class & args]
  (let [seeded-instance (create-seeded-instance class args)]
    (apply-message-to class seeded-instance :add-instance-values args)
  )
)

(defn get-class-from [instance]
  (eval (:__class_symbol__ instance))
)

(defn send-to [instance message & args]
  (let [class (get-class-from instance)]
    (apply-message-to class instance message args))
)

(def a-point (make Point 0 0))

(apply-message-to Point a-point :shift [1 3])

;; Exercise 2

(def Point
{
  :__own_symbol__ 'Point
  :__instance_methods__
  {
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    :class-name :__class_symbol__
    :class (fn [this]
             (get-class-from this))
    :shift (fn [this xinc yinc]
             (make Point (+ (:x this) xinc)
                         (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

(def point (make Point 1 2))
(send-to point :class-name)
(send-to point :class)

;; Exercise 3

(def point (make Point 1 2))

(def Point
{
  :__own_symbol__ 'Point
  :__instance_methods__
  {
    :origin (fn [this] (make Point 0 0))
    :add-instance-values (fn [this x y]
                           (assoc this :x x :y y))
    :class-name :__class_symbol__
    :class (fn [this]
             (get-class-from this))
    :shift (fn [this xinc yinc]
             (make Point (+ (:x this) xinc)
                         (+ (:y this) yinc)))
    :add (fn [this other]
           (send-to this :shift (:x other)
                                (:y other)))
   }
 })

(send-to point :origin)

;; This works because we dynamically eval the class of point
;; to the newly defined Point class.

;; Exercise 4

(def Holder
{
  :__own_symbol__ 'Holder
  :__instance_methods__
  {
    :add-instance-values (fn [this held]
                           (assoc this :held held))
  }
})

(:not-there {:a 1, :b 2})
(and true nil)
(and true false)
(or nil 3)

(defn apply-message-to [class instance message args]
  (let [method (or (get-method-from message class)
                message)]
    (apply method instance args)
  )
)

(send-to (make Holder "stuff") :held)

;; Exercise 5

(send-to (make Point 1 2) :some-unknown-message)

;; ------------ Week 7 -------------- ;;

;; Inheritance (and Recursion)

;; learning to use assert for pre-conditions

;; defining a superclass "Anything"
(def Anything {
  :__own_symbol__ 'Anything
  :__instance_methods__
  {
    ;; default constructor
    :add-instance-values identity
    ;; these two methods have been pulled up from Point.
    :class-name :__class_symbol__
    :class (fn [this] (get-class-from this)) }
})

;; defining a way to do overriding/inheritance
;; (def maps (map class-instance-methods (lineage 'Point)))
;; (def merged (apply merge maps))

;; QUOTE ;;
;; I picked the rather odd name method-cache to
;; give me an excuse to point out that this
;; implementation isn’t completely silly.

;; Recursion
;; -- why is the book patronising about this :S ?!?!

;; -- patter of recursion in Clojure with if:
(defn recursive-example [num]
  (if (<= num 1)
    1
    (+ num (recursive-example (dec num)))
  )
)

(recursive-example 1)
(recursive-example 10)

(doc cons)

(defn recursive-bottom-up-helper
  [number step firstNum secondNum list-so-far]
  (if (<= number step)
    list-so-far
    (recursive-bottom-up-helper number
                                (inc step)
                                secondNum
                                (+ firstNum secondNum)
                                (concat list-so-far (list(+ firstNum secondNum))))
  )
)

(defn fibonaci [number]
  (if (<= number 2)
    (list 1 1)
    (recursive-bottom-up-helper number 2 1 1 (list 1 1))
  )
)

(fibonaci 10)

;; optimising tail recursion - use recur() method?

;; Exercise 1

(defn factorial [n]
  (if (or (== 0 n) (== 1 n))
    1
    (* n (factorial (dec n)))
  )
)


(factorial 4)

;; Exercise 2

(def recursive-function
  (fn [something so-far]
    (if (= 0 something)
      so-far
      (recursive-function (dec something)
                             (* something so-far)))))

(defn factorial [n]
  (recursive-function n 1)
)

(factorial 4)

;; Exercise 3

(defn recursive-function [list so-far]
  (if (empty? list)
    so-far
    (recursive-function (rest list)
                        (+ (first list) so-far))
  )
)

(recursive-function [1 2 3 4] 0)

;; Exercise 4

(defn recursive-function [list so-far]
  (if (empty? list)
    so-far
    (recursive-function (rest list)
                        (* (first list) so-far))
  )
)

(recursive-function [1 2 3 4] 1)

(defn recursive-function [function list so-far]
  (if (empty? list)
    so-far
    (recursive-function function
                            (rest list)
                            (function (first list) so-far))
  )
)

(recursive-function + [1 2 3 4] 0)
(recursive-function * [1 2 3 4] 1)

;; Exercise 5


(defn add-to-map [key-word map-so-far]
  (assoc map-so-far key-word 0)
)

(recursive-function add-to-map
                    [:a :b :c]
                    {}
)

(defn add-to-map-with-position [key-word map-so-far]
  (assoc map-so-far key-word (count map-so-far))
)

(recursive-function add-to-map-with-position
                    [:a :b :c]
                    {}
)






