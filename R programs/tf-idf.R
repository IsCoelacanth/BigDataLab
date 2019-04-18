library(tm)
library(proxy)
library(dplyr)
library(ggplot2)
library(corrplot)

print("The dataset: each sentence is one docoment")
doc <- c( "The sky is blue.", "The sun is bright today.", "The sun in the sky is bright.", "We can see the shining sun, the bright sun.", "The moon is full, the sky full of stars.", "The sky was dark, the stars plentiful and bright.", "The sun is but a morning star.")
corpus <- Corpus( VectorSource(doc) )
controlList <- list(removePunctuation = TRUE, stopwords = TRUE, tolower = TRUE)

print("computing the term-frequency matrix: ")
(tf <- as.matrix(TermDocumentMatrix(corpus, control = controlList) ) )
corrplot(tf, method = "number", is.corr = FALSE, cl.pos = "n")

print("computing the idf, and then converting into a diagonal matrix (used later)")
(idf <- log(ncol(tf) / (1 + rowSums(tf != 0) ) ) )
(idf <- diag(idf) )

print("calculating the final tf-idf matrix")
tf_idf <- crossprod(tf, idf)
colnames(tf_idf) <- rownames(tf)
(tf_idf <- tf_idf / sqrt(rowSums(tf_idf^2) ) )
corrplot(tf_idf, method = "number")
