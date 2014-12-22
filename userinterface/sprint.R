require(XML)
require(tm)
require(wordcloud)
require(RColorBrewer)
words=c("Sprint","consumer","bill","network","expand","area","technology","project","call","bring","capacity")
freq=c(20000,17093,15939,12032,9375,8835,6932,4231,4103,3082,2124)
wordcloud(words,freq, scale=c(3,.2),min.freq=1,max.words=Inf, random.order=FALSE, rot.per=.15,colors='green')



