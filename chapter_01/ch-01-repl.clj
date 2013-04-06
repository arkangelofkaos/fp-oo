;; How can this be saved as a Clojure file :S?
(use 'clojure.repl)

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

;; How do you do pattern matching in Clojure?
(def myNth
  (fn [list n] ())
)

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

(def add-squares
  (fn [& numbers]
    ()
  )
)

(map inc [0 1 2 3])
(map * [0 1 2 3] [100 200 300 400])




