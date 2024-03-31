package errors

import exceptions.IExError


sealed class CommonError(
    override val message: String,
    override val errorType: ErrorType,
    override val errors: Map<String, String>,
) : IExError, RuntimeException()
