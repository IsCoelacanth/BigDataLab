require(MASS)
require(ggplot2)

set.seed(42)
set1 = MASS::mvrnorm(n = 150, c(-10,10), matrix(c(1.5,1,1,1.5),2))
set2 = MASS::mvrnorm(n = 150, c(-5,10), matrix(c(1,2,2,6),2))
set3 = MASS::mvrnorm(n = 150, c(-1,1), matrix(c(4,0,0,4),2))
set4 = MASS::mvrnorm(n = 150, c(10,-10), matrix(c(4,0,0,4),2))
set5 = MASS::mvrnorm(n = 150, c(3,-3), matrix(c(4,0,0,4),2))
DF = data.frame(rbind(set1,set2,set3,set4,set5),cluster=as.factor(c(rep(1:5,each=150))))
ggplot(DF,aes(x=X1,y=X2,color=cluster))+geom_point()

kmeans = function(data,K=4,stop_crit=10e-3)
{
  #Initialisation of clusters
  centroids = data[sample.int(nrow(data),K),]
  current_stop_crit = 1000
  cluster = rep(0,nrow(data))
  converged = F
  it = 1
  while(current_stop_crit>=stop_crit & converged==F)
  {
    it=it+1
    if (current_stop_crit<=stop_crit)
    {
      converged=T
    }
    old_centroids=centroids
    ##Assigning each point to a centroid
    for (i in 1:nrow(data))
    {
      min_dist=10e10
      for (centroid in 1:nrow(centroids))
      {
        distance_to_centroid=sum((centroids[centroid,]-data[i,])^2)
        if (distance_to_centroid<=min_dist)
        {
          cluster[i]=centroid
          min_dist=distance_to_centroid
        }
      }
    }
    ##Assigning each point to a centroid
    for (i in 1:nrow(centroids))
    {
      centroids[i,]=apply(data[cluster==i,],2,mean)
    }
    current_stop_crit=mean((old_centroids-centroids)^2)
  }
  return(list(data=data.frame(data,cluster),centroids=centroids))
}

res<-kmeans(DF[1:2],K=5)
res$centroids$cluster=1:5
res$data$isCentroid=F
res$centroids$isCentroid=T
data_plot=rbind(res$centroids,res$data)
ggplot(data_plot,aes(x=X1,y=X2,color=as.factor(cluster),size=isCentroid,alpha=isCentroid))+geom_point()
