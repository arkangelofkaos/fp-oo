;; Add extension .clj to make this save as a clojure file
(use 'clojure.repl)

;; ------------ Week 1 -------------- ;;

;; Code from chapter
(if true 5)
(+ 1 2)
"hi mom!"
true
*file*
+
(odd? 2)
(number? +)
(+ 1 (- 4 2))
((fn [n] (+ n n)) 4)
(def twice (fn [n] (+ n n)))
(twice 10)

;; Exercise 0
(def myList '(1 2 3))
(rest myList)
(nth myList 1)

;; Exercise 1
(def second (fn [list] (nth list 1)))
(second myList)

;; Exercise 2
(def third 
  (fn [list] (nth list 2))
)
(third myList)

(def third-2nd-impl
  (fn [list] (first 
              (rest 
               (rest list))) 
  )
)
(third-2nd-impl myList)

;; ------------ Week 2 -------------- ;;

;; a vector
[1 2 3 4]

(rest [1 2 3])
(= [2 3] (rest [1 2 3]))

[inc dec]

'[inc dec]

(if (odd? 3) 
  (prn "Odd!")
  (prn "Even!"))

(def my-apply
(fn [function sequence]
(eval (cons function sequence))))

(my-apply + [1 2 3 4 5])

(map inc [0 1 2 3])
(map * [0 1 2 3] [100 200 300 400])

;; Exercise 3
(def add-squares
  (fn [& numbers]
    (apply + (map * numbers numbers))
  )
)

(add-squares 1)
(add-squares 2)
(add-squares 1 2)

;; Exercise 3.5
(defn add-all 
  [seq] 
  (apply + seq)
)

(defn squares [seq]
  (map * seq seq)
)

(add-all `(1 2))
(squares `(1 2 3))
(add-all (squares `(1 2 3)))

(def add-squares
  (fn [& numbers]
    (add-all (squares numbers))
  )
)

(add-squares 1 2)

;; Exercise 4
(defn myFactorial [n] 
  (apply * (range 1 (+ 1 n))) 
)

(myFactorial 1)
(myFactorial 2)
(myFactorial 3)
(myFactorial 4)
(myFactorial 5)

;; Exercise 5
(doc take)
;; -> Use take to return first N prime numbers
;; [assume pre-defined prime number list]
(def myPrimeNumberList `(2 3 5 7 9 11 13 17 19 23))
(defn firstNPrimeNumbers [n] take n myPrimeNumberList)
(firstNPrimeNumbers 6)

(doc distinct)
;; -> Get sum of all distinct numbers
(defn sumDistinct [& numbers] 
  (apply + (distinct numbers))
)
(sumDistinct 1 2 3 3 4 1 2 3 4)

(doc concat)
(concat `(1 2) `(3 4) `(5 6))

(doc repeat)
(repeat 2 5)

(doc interleave)
(interleave (repeat 1) (repeat 2) (repeat 3))
;; (apply repeat (range 0 1)) 

(doc drop)
(drop 2 `(1 2 3))

(doc drop-last)
(drop-last `(1 2 3))

(defn drop-first-and-last [seq]
 (drop 1 (drop-last seq))
)
(drop-first-and-last `(1 2 3 4))

(doc flatten)
(def myNestedList `((1 2 (4)) (3) (1) 1 2))
(flatten myNestedList)

(doc partition)
(partition 2 [1 2 3 4])

(doc every?)
(defn every-element-even? [seq]
  (every? (fn [x] (even? x)) seq)
)

(every-element-even? `(1 2))
(every-element-even? `(6 2))

(doc remove)
(def even-numbers-in-range 
  (fn [n] 
    (remove odd? (range 1 (inc n)))
  )
)
(even-numbers-in-range 10)

;; Exercise 6
(doc and)
(and true true)
(doc apply)

;- How do you do symbolic and in clojure?
(defn prefix-of? [candidate sequence]
  (apply = true (map = candidate sequence))
)
(prefix-of? `(1 2) `(1 2 3))

(prefix-of? `(3 4) `(1 2 3))

(doc reduce)

;; Exercise 7
(defn tails [seq] 
  (map drop
   (range (inc (count seq)))
   (repeat seq)
  )
)
(tails `(1 2 3 4))
(doc drop)
(doc repeat)

;; Exercise 8
(def puzzle (fn [list] (list list)))
; (puzzle `(1 2 3)) 
; fails as list is identified as a variable 
; rather than the function keyword























