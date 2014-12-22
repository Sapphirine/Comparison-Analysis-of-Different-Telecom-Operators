require(XML)
require(tm)
require(wordcloud)
require(RColorBrewer)
words=c("AT&T","sale","data","energy","radial","property","system","houses","die","points","velocity","information","technology","united","exchange","city","international")
freq=c(20000,19974,10737, 9971,9123,9078,8839,8358,8190,7904,7783,7602,7385,6343,6322,6126,5991)
wordcloud(words,freq, scale=c(2,.1),min.freq=1,max.words=Inf, random.order=FALSE, rot.per=.15,colors='red')

