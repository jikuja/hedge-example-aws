(set-env! :source-paths #{"src"}
          :resource-paths #{"resources"}
          :dependencies '[[org.clojure/clojurescript "1.9.946"]
                          [adzerk/boot-cljs "1.7.228-2" :scope "test"]
                          [crisptrutski/boot-cljs-test "0.3.4" :scope "test"]
                          [siili/boot-hedge "0.1.2" :scope "test"]
                          [siili/hedge "0.1.2"]
                          [binaryage/oops "0.5.8"]
                          [org.clojars.akiel/async-error "0.2"]])

(require '[boot-hedge.core :as hedge])
(hedge/hedge-init!)

(require '[crisptrutski.boot-cljs-test :refer [test-cljs report-errors!] :as cljs-test])

(deftask testing [] (set-env! :source-paths #(conj % "test")) identity)
(ns-unmap 'boot.user 'test)

(deftask test []
  (comp (testing)
        (test-cljs :js-env :node
                   :exit?  true)))
