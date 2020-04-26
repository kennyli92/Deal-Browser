package com.target.dealbrowserpoc.dagger.module

import com.target.dealbrowserpoc.dagger.scope.ViewScope
import com.target.dealbrowserpoc.deals.data.DealsApi
import com.target.dealbrowserpoc.deals.data.DealsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ViewModule {
  @ViewScope
  @Provides
  fun providesDealsApi(
    retrofit: Retrofit
  ): DealsApi {
    return retrofit.create(DealsApi::class.java)
  }

  @ViewScope
  @Provides
  fun providesDealsRepository(
    dealsApi: DealsApi
  ): DealsRepository {
    return DealsRepository(dealsApi = dealsApi)
  }
}