setwd("~/Desktop/CSE/373/HW/HW2/project1-mahirb-zmcnulty/experimentdata")

dat = read.csv("experiment1.csv")
dictSize <- myDat$InputDictionarySize
test1_results <- myDat$Test1Results
test2_results <- myDat$Test2Results


plot(dictSize, test1_results, col = "red")
plot(dictSize, test2_results)

?plot
