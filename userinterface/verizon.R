require(XML)
require(tm)
require(wordcloud)
require(RColorBrewer)
words=c("Verizon","price","cover","usage","device","package","cost","home","customer","people","channel")
freq=c(20000,15833,12998,11987,10233,9212,8873,8632,6392,5124,3988)
wordcloud(words,freq, scale=c(3,.1),min.freq=1,max.words=Inf, random.order=FALSE, rot.per=.15,colors='blue')


