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

(def Point {
  :__own_symbol__ 'Point
  :__instance_methods__
    {
      :add-instance-values
        (fn [this x y]
          (assoc this :x x :y y))
      :shift
        (fn [this xinc yinc]
          (make Point 
             (+ (:x this) xinc)
             (+ (:y this) yinc)))
    }
})

(def make
  (fn [class & args]
    (let [allocated {}
          seeded (assoc allocated
                        :__class_symbol__ (:__own_symbol__ class))
          constructor (:add-instance-values
                       (:__instance_methods__ class))]
         (apply constructor seeded args))))

(def send-to
  (fn [instance message & args]
    (let [class (eval (:__class_symbol__ instance)) 
          method (message (:__instance_methods__ class))]
      (apply method instance args))))

(def apply-message-to
(fn [class instance message args]
       (message (:__instance_methods__ class))))

(def a-point (make Point 0 0))

(apply-message-to Point a-point :shift [1 3])

;; ------------ Week 6 -------------- ;;

(+ 1 3)

;; Inheritance (and Recursion)




















